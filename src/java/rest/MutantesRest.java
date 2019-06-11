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
import java.util.ArrayList;
import java.util.LinkedList;
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
    
    @javax.ws.rs.GET
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    @Path("/")
    public String listarMutantes() throws Exception {
        dao.MutanteDAO mutanteDAO = new dao.MutanteDAO();
        List<Mutante> lista = mutanteDAO.listar();
        
        JsonObject jsonObject = new JsonObject();
        
        Gson gson = new GsonBuilder().create();
        JsonArray jsonArray = gson.toJsonTree(lista).getAsJsonArray();
        JsonObject userJsonObject = new JsonObject();
        jsonObject.add("mutantes", jsonArray);
        //JsonObject usersJsonObject = new JsonObject();
        //jsonObject.add("users", usersJsonObject);
        return jsonObject.toString();
    }
    
    @javax.ws.rs.GET
    @javax.ws.rs.Produces(javax.ws.rs.core.MediaType.APPLICATION_JSON)
    @Path("{id}")
    public String obterMutante(@PathParam("id") String id) throws Exception {
        dao.MutanteDAO mutanteDAO = new dao.MutanteDAO();
        Mutante mutante = mutanteDAO.obter(Integer.valueOf(id));
        
        JsonObject jsonObject = new JsonObject();
        
        Gson gson = new GsonBuilder().create();
        //JsonArray jsonArray = gson.toJsonTree(lista).getAsJsonArray();
        //JsonObject userJsonObject = new JsonObject();
        //jsonObject.add("mutantes", jsonArray);
        


        //JsonObject usersJsonObject = new JsonObject();
        //jsonObject.add("users", usersJsonObject);
        return gson.toJson(mutante);
        //return jsonObject.toString();
    }
    
    @POST
    @Path("/novo")
    @Consumes(MediaType.APPLICATION_JSON)
    @javax.ws.rs.Produces(MediaType.APPLICATION_JSON)
    public String salvar(String json) throws JSONException {
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
        
        JSONObject object = new JSONObject();
        object.put("id", id);
        String jsonResult = object.toString();
        System.out.println(jsonResult);
        return jsonResult;
    }

 
}
