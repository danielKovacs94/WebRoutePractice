package com.company;

import java.io.IOException;
import java.io.OutputStream;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.HashMap;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class Test {

    public static HashMap<String, Method> routes =  new HashMap<String, Method>();

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        createContext(server);
        server.setExecutor(null); // creates a default executor
        server.start();
    }

    public static void createContext(HttpServer server){
        for(Method method: Routes.class.getMethods()) {
            if(method.isAnnotationPresent(WebRoute.class)) {
                WebRoute annotation = method.getAnnotation(WebRoute.class);
                System.out.println("Annotation: " + annotation.toString());
                String path = annotation.value();
                routes.put(path, method);
                server.createContext(path, new MyHandler());
            }
        }

    }

    static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            System.out.println("Path: " + t.getHttpContext().getPath());
            Method methodToExecute = routes.get(t.getHttpContext().getPath());
            String crud = t.getRequestMethod();

            String response = null;
            try {
                response = (String) methodToExecute.invoke(new Routes(), crud );
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }

            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}