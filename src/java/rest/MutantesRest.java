/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rest;

import beans.Mutante;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dao.MutanteDAO;
import java.util.List;
import java.lang.reflect.Type;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author SAMUEL
 */
@javax.ws.rs.Path("/mutantes")
public class MutantesRest {
    private final String dir = java.nio.file.Paths.get(".").toAbsolutePath().normalize().toString();
    
    @javax.ws.rs.GET
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    @Path("/")
    public String listarMutantes() throws Exception {
        System.out.println(java.nio.file.Paths.get(".").toAbsolutePath().normalize().toString());
        dao.MutanteDAO mutanteDAO = new dao.MutanteDAO();
        List<Mutante> lista = mutanteDAO.listar();
        for (Mutante m : lista) {
            File f =  new File(dir + "/" + m.getId() + ".png");
            String encodstring = encodeFileToBase64Binary(f);
            m.setFoto(encodstring);
        }
        JsonObject jsonObject = new JsonObject();
        Gson gson = new GsonBuilder().create();
        JsonArray jsonArray = gson.toJsonTree(lista).getAsJsonArray();
        JsonObject userJsonObject = new JsonObject();
        jsonObject.add("mutantes", jsonArray);
        return jsonObject.toString();
    }
    
    @javax.ws.rs.GET
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    @Path("{id}")
    public String obterMutante(@PathParam("id") String id) throws Exception {
        dao.MutanteDAO mutanteDAO = new dao.MutanteDAO();
        Mutante mutante = mutanteDAO.obter(Integer.valueOf(id));
        File f =  new File(dir + "/" + mutante.getId() + ".png");
        String encodstring = encodeFileToBase64Binary(f);
        mutante.setFoto(encodstring);
        JsonObject jsonObject = new JsonObject();
        Gson gson = new GsonBuilder().create();
        return gson.toJson(mutante);
    }
    
    @javax.ws.rs.GET
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    @Path("/remover/{id}")
    public String removerMutante(@PathParam("id") String id) throws Exception {
        dao.MutanteDAO mutanteDAO = new dao.MutanteDAO();
        int idResult = mutanteDAO.remover(Integer.valueOf(id));
        JSONObject object = new JSONObject();
        object.put("id", idResult);
        String jsonResult = object.toString();
        System.out.println(jsonResult);
        return jsonResult;
    }
    
    @javax.ws.rs.GET
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    @Path("/listar/{habilidade}")
    public String listarPorHabilidade(@PathParam("habilidade") String habilidade) throws Exception {
        System.out.println(habilidade);
        dao.MutanteDAO mutanteDAO = new dao.MutanteDAO();
        List<Mutante> lista = mutanteDAO.listarPorHabilidade(habilidade);
        JsonObject jsonObject = new JsonObject();
        Gson gson = new GsonBuilder().create();
        JsonArray jsonArray = gson.toJsonTree(lista).getAsJsonArray();
        JsonObject userJsonObject = new JsonObject();
        jsonObject.add("mutantes", jsonArray);
        return jsonObject.toString();
    }
    
    @POST
    @Path("/novo")
    @Consumes(MediaType.APPLICATION_JSON)
    @javax.ws.rs.Produces(MediaType.APPLICATION_JSON)
    public String salvar(String json) throws JSONException, IOException, FileNotFoundException {
        MutanteDAO mutanteDAO = new MutanteDAO();
        JSONObject jsonObject = new JSONObject(json);
        List<String> habilidades = new ArrayList<String>();
        if (jsonObject.getString("habilidade1").length() > 0) {
            habilidades.add(jsonObject.getString("habilidade1"));
        }
        if (jsonObject.getString("habilidade2").length() > 0) {
            habilidades.add(jsonObject.getString("habilidade2"));
        }
        if (jsonObject.getString("habilidade3").length() > 0) {
            habilidades.add(jsonObject.getString("habilidade3"));
        }
 
        Mutante m = new Mutante();
        m.setNome(jsonObject.getString("nome"));
        
        int id = mutanteDAO.cadastrar(m, habilidades);
        if (jsonObject.getString("imagem").length() > 0) {
            //System.out.println(jsonObject.getString("imagem"));
            byte[] decodedImg =  Base64.getMimeDecoder().decode(jsonObject.getString("imagem"));
            try {
               System.out.println("dir: " + dir);
               FileOutputStream fos = new FileOutputStream(dir + "/" + id + ".png");
               fos.write(decodedImg);
               fos.close();
            } catch (FileNotFoundException e) {
                System.out.println(e.getMessage());
            }
        }
        JSONObject object = new JSONObject();
        object.put("id", id);
        String jsonResult = object.toString();
        System.out.println(jsonResult);
        return jsonResult;
    }
    
    private static String encodeFileToBase64Binary(File file) throws IOException{
            String encodedfile = null;
            try {
                java.io.FileInputStream fileInputStreamReader = new java.io.FileInputStream(file);
                byte[] bytes = new byte[(int)file.length()];
                fileInputStreamReader.read(bytes);
                encodedfile = new String(Base64.getMimeEncoder().encode(bytes));
            } catch (java.io.FileNotFoundException e) {
                // TODO Auto-generated catch block
                encodedfile = "";
            }

            return encodedfile;
        }

 
}
