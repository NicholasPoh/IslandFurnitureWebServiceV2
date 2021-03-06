package service;

import DBaccess.lineitementityDB;
import DBaccess.salesrecordentityDB;
import Entity.ShoppingCartLineItem;
import java.net.URI;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("commerce")
public class ECommerceFacadeREST {

    @Context
    private UriInfo context;

    public ECommerceFacadeREST() {
    }

    @GET
    @Produces("application/json")
    public String getJson() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

    /**
     * PUT method for updating or creating an instance of ECommerce
     *
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/json")
    public void putJson(String content) {
    }
    
     @POST
    @Path("createSalesRecord")
    @Consumes("application/x-www-form-urlencoded")
    public Response createSalesRecord(
        @FormParam("memberId") int memberId,
        @FormParam("storeId") int storeId,
        @FormParam("transactionAmount") double transactionAmount){
        try{
            salesrecordentityDB srdb = new salesrecordentityDB();
            int srId = srdb.createSalesRecordDB(memberId, storeId, transactionAmount);
            return Response.status(200).entity(srId).build();
        }
        catch(Exception e){
            e.printStackTrace();
            return Response.status(500).build();
        }
    }
    
    @POST
    @Path("createLineItemRecord")
    @Consumes("application/json")
    public Response createLineItemRecord(
        @QueryParam("srId") int srId,
        @QueryParam("itemId") int itemId, 
        @QueryParam("itemQuantity") int itemQuantity) {
        try{
            lineitementityDB lidb = new lineitementityDB();
            boolean liSuccess = lidb.createLineItemRecordDB(srId, itemId, itemQuantity);
            return (liSuccess)? Response.status(200).build(): Response.status(500).build();
        }
         catch(Exception e){
            e.printStackTrace();
            return Response.status(500).build();
        }
    }

    @POST
    @Path("stockUpdate")
    @Consumes("application/json")
    public Response stockUpdate(
        @QueryParam("itemId") int itemId, 
        @QueryParam("itemQuantity") int itemQuantity,
        @QueryParam("itemName") String itemName){
        try{
            lineitementityDB lidb = new lineitementityDB();
            boolean updateStockSuccess = lidb.updateStockDB(itemId, itemQuantity, itemName);
            return (updateStockSuccess)? Response.status(200).build(): Response.status(500).build();
        }
        catch(Exception e){
            e.printStackTrace();
            return Response.status(400).build();
        }
    }
    
}
