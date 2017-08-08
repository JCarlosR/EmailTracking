package com.example.neyser.emailtracking.ui.fragment;

import android.app.Activity;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TextView;

import com.example.neyser.emailtracking.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ReportsFragment extends Fragment {
    TextView a_resultadoCC, a_resultadoCT;
    EditText a_txtApellido;
    private Button a_btnConsultarCC, a_btnCancelarCC,a_btnConsultarCT;

    String IP = "http://192.168.1.36/WS_EmailAndroid/";
    String CAC = IP + "cliente_correo.php";
    String CACBYID = IP + "cliente_correo_ape.php";

    ObtenerWebService hiloconexion;
    ObtenerWebServiceTodos hiloconexionT;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_reports, container, false);

        a_btnConsultarCC = (Button) view.findViewById(R.id.btnConsultarCCA);
        a_btnCancelarCC = (Button) view.findViewById(R.id.btnCancelarCCA);
        a_btnConsultarCT = (Button) view.findViewById(R.id.btnConsultarCTA);

        a_resultadoCC = (TextView) view.findViewById(R.id.txtResultadoCA);
        a_resultadoCT = (TextView) view.findViewById(R.id.txtResultadoCTA);

        a_txtApellido = (EditText) view.findViewById(R.id.txtApellidoF);

        Resources res = getResources();

        TabHost tabs=(TabHost)view.findViewById(android.R.id.tabhost);
        tabs.setup();

        TabHost.TabSpec spec=tabs.newTabSpec("mitab1");
        spec.setContent(R.id.tab1);
        spec.setIndicator("Buscar Cliente",
                res.getDrawable(android.R.drawable.ic_btn_speak_now));
        tabs.addTab(spec);

        spec=tabs.newTabSpec("mitab2");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Consulta todos",
                res.getDrawable(android.R.drawable.ic_dialog_map));
        tabs.addTab(spec);

        tabs.setCurrentTab(0);

        a_btnConsultarCC.setOnClickListener( new View.OnClickListener() {
            Activity activity = getActivity();
            public void onClick(View view){
                hiloconexion = new ObtenerWebService();
                String cadenallamada = CACBYID + "?apellido=" + a_txtApellido.getText().toString();
                hiloconexion.execute(cadenallamada,"2");   // Parámetros que recibe doInBackground
            }

        });

        a_btnConsultarCT.setOnClickListener( new View.OnClickListener() {
            public void onClick(View view){
                hiloconexionT = new ObtenerWebServiceTodos();
                hiloconexionT.execute(CAC,"3");   //
            }

        });

        return view;
    }


    public class ObtenerWebService extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... params) {

            String cadena = params[0];
            URL url = null; // Url de donde queremos obtener información
            String devuelve="";


            if(params[1]=="2"){    // ingresar

                try {
                    url = new URL(cadena);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection(); //Abrir la conexión
                    connection.setRequestProperty("User-Agent", "Mozilla/5.0" +
                            " (Linux; Android 1.5; es-ES) Ejemplo HTTP");
                    //connection.setHeader("content-type", "application/json");

                    int respuesta = connection.getResponseCode();
                    StringBuilder result = new StringBuilder();

                    if (respuesta == HttpURLConnection.HTTP_OK){


                        InputStream in = new BufferedInputStream(connection.getInputStream());  // preparo la cadena de entrada

                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));  // la introduzco en un BufferedReader

                        // El siguiente proceso lo hago porque el JSONOBject necesita un String y tengo
                        // que tranformar el BufferedReader a String. Esto lo hago a traves de un
                        // StringBuilder.

                        String line;
                        while ((line = reader.readLine()) != null) {
                            result.append(line);        // Paso toda la entrada al StringBuilder
                        }

                        //Creamos un objeto JSONObject para poder acceder a los atributos (campos) del objeto.
                        JSONObject respuestaJSON = new JSONObject(result.toString());   //Creo un JSONObject a partir del StringBuilder pasado a cadena
                        //Accedemos al vector de resultados
                        String resultJSON = respuestaJSON.getString("estado");   // estado es el nombre del campo en el JSON



                        if (resultJSON=="1"){      // hay alumnos a mostrar
                            JSONArray alumnosJSON = respuestaJSON.getJSONArray("abiertosApe");   // estado es el nombre del campo en el JSON
                            for(int i=0;i<alumnosJSON.length();i++){
                                devuelve = devuelve +"Categoria: " +alumnosJSON.getJSONObject(i).getString("categoria") +"\n"+
                                        "Titulo: " + alumnosJSON.getJSONObject(i).getString("titulo") +"\n"+
                                        "Cliente: " + alumnosJSON.getJSONObject(i).getString("cliente") +"\n"+
                                        "Sist.Opera: " + alumnosJSON.getJSONObject(i).getString("sistema") +"\n"+
                                        "Navegador: " + alumnosJSON.getJSONObject(i).getString("navegador") +"\n"+
                                        "IP: " + alumnosJSON.getJSONObject(i).getString("ip") +"\n"+
                                        "Fecha: " + alumnosJSON.getJSONObject(i).getString("fecha") +"\n"+
                                        "------------------------------------------------------\n";
                            }

                        }
                        else if (resultJSON=="2"){
                            devuelve = "No hay alumnos";
                        }

                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return devuelve;


            }
            return null;
        }

        @Override
        protected void onCancelled(String s) {
            super.onCancelled(s);
        }

        @Override
        protected void onPostExecute(String s) {

            a_resultadoCC.setText(s);
            //super.onPostExecute(s);

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }




    public class ObtenerWebServiceTodos extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... params) {

            String cadena = params[0];
            URL url = null; // Url de donde queremos obtener información
            String devuelve="";


            if(params[1]=="3"){    // Consulta de todos los alumnos

                try {
                    url = new URL(cadena);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection(); //Abrir la conexión
                    connection.setRequestProperty("User-Agent", "Mozilla/5.0" +
                            " (Linux; Android 1.5; es-ES) Ejemplo HTTP");
                    //connection.setHeader("content-type", "application/json");
                    connection.connect();

                    int respuesta = connection.getResponseCode();
                    StringBuilder result = new StringBuilder();

                    if (respuesta == HttpURLConnection.HTTP_OK){


                        InputStream in = new BufferedInputStream(connection.getInputStream());  // preparo la cadena de entrada

                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));  // la introduzco en un BufferedReader

                        // El siguiente proceso lo hago porque el JSONOBject necesita un String y tengo
                        // que tranformar el BufferedReader a String. Esto lo hago a traves de un
                        // StringBuilder.

                        String line;
                        while ((line = reader.readLine()) != null) {
                            result.append(line);        // Paso toda la entrada al StringBuilder
                        }

                        //Creamos un objeto JSONObject para poder acceder a los atributos (campos) del objeto.
                        JSONObject respuestaJSON = new JSONObject(result.toString());   //Creo un JSONObject a partir del StringBuilder pasado a cadena
                        //Accedemos al vector de resultados

                        String resultJSON = respuestaJSON.getString("estado");   // estado es el nombre del campo en el JSON



                        if (resultJSON=="1"){      // hay alumnos a mostrar
                            JSONArray alumnosJSON = respuestaJSON.getJSONArray("correo");   // estado es el nombre del campo en el JSON
                            for(int i=0;i<alumnosJSON.length();i++){
                                devuelve = devuelve +"Categoria: " +alumnosJSON.getJSONObject(i).getString("categoria") +"\n"+
                                                        "Titulo: " + alumnosJSON.getJSONObject(i).getString("titulo") +"\n"+
                                                         "Cliente: " + alumnosJSON.getJSONObject(i).getString("cliente") +"\n"+
                                                        "Sist.Opera: " + alumnosJSON.getJSONObject(i).getString("sistema") +"\n"+
                                                        "Navegador: " + alumnosJSON.getJSONObject(i).getString("navegador") +"\n"+
                                                        "IP: " + alumnosJSON.getJSONObject(i).getString("ip") +"\n"+
                                                        "Fecha: " + alumnosJSON.getJSONObject(i).getString("fecha") +"\n"+
                                                         "------------------------------------------------------\n";
                            }

                        }
                        else if (resultJSON=="2"){
                            devuelve =("No hay alumnos");
                        }


                    }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return devuelve;


            }

            return null;
        }

        @Override
        protected void onCancelled(String s) {
            super.onCancelled(s);
        }

        @Override
        protected void onPostExecute(String s) {
            a_resultadoCT.setText(s);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }


}











