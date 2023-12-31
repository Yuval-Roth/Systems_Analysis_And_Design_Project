package utils;

import exceptions.ErrorOccurredException;

import java.lang.reflect.Type;

public class Response {

    private final String message;
    private final boolean success;
    private final String data;

    /**
     * The data parameter will be serialized using {@link JsonUtils#serialize(Object)}
     */
    public <T> Response(String message, boolean success, T data) {
        this.message = message;
        this.success = success;
        this.data = JsonUtils.serialize(data);
    }

    /**
     * this constructor takes a string as data and does not serialize it
     */
    public Response(String message, boolean success, String data) {
        this.message = message;
        this.success = success;
        this.data = data;
    }

    /**
     * This constructor is used when there is no data to be sent. The data field will be an empty string.
     */
    public Response(String message, boolean success) {
        this(message, success, "");
    }

    /**
     * @param success If the request was successful or not
     */
    public Response(boolean success) {
        this("", success, "");
    }

    public <T> Response(boolean success,T data) {
        this("", success, data);
    }

    public String message() {
        return message;
    }

    public boolean success() {
        return success;
    }

    /**
     * @param typeOfT Type of the object for deserialization
     * @return Deserialized object of type T
     * @apiNote If you want to get the raw data as a string, use {@link #data()} instead
     * <br/><br/><br/>examples for a type definition:<br/><br/><code>1) Type type = new TypeToken&lt;LinkedList&lt;SomeClass&gt;&gt;(){}.getType();</code>
     * <br/><br/><code>2) Type type = SomeClass.class;</code>
     */
    public <T> T data(Type typeOfT) {
        if(data == null) {
            return null;
        }
        return JsonUtils.deserialize(data,typeOfT);
    }

    /**
     * @return Raw data as a string. Object must be deserialized manually
     * @apiNote If you want to get a deserialized object Use {@link #data(Type)} instead
     */
    public String data(){
        return data;
    }

    public Integer dataToInt(){
        return Integer.parseInt(data);
    }

    public Boolean dataToBoolean(){
        return Boolean.parseBoolean(data);
    }

    /**
     * This method will serialize the response object using {@link JsonUtils#serialize(Object)}
     */
    public String toJson(){
        return JsonUtils.serialize(this);
    }

    /**
     * This method will deserialize the json string using {@link JsonUtils#deserialize(String, Type)}
     */
    public static Response fromJson(String json){
        return JsonUtils.deserialize(json, Response.class);
    }
    public static Response fromJsonWithValidation(String json) throws ErrorOccurredException {
        Response response = JsonUtils.deserialize(json, Response.class);
        if (response.success == false){
            String causeMessage = response.data == null ? "" : response.data;
            String message = response.message == null ? "" : response.message;
            throw new ErrorOccurredException(message, new Throwable(causeMessage));
        }
        return response;
    }

    /**
     * This method will return a response object with the message as the exception message and success as false.
     * @apiNote if the exception has a cause, the cause message will be added to the response object in the data field as a string.
     * Otherwise, the data field will be an empty string
     */
    public static Response getErrorResponse(Exception e){

        String cause = "";
        if(e.getCause() != null){
            cause = e.getCause().getMessage();
        }
        return new Response(e.getMessage(), false, cause);
    }
}
