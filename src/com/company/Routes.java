package com.company;

public class Routes {

    @WebRoute("/test1")
    public String test1(String met){

        if("POST".equals(met)){
            return "POST test1";
        }else{
            return "GET test1";
        }

    }

    @WebRoute("/test2")
    public String test2(String method){

        if("POST".equals(method)){
            return "POST test2";
        }else{
            return "GET test2";
        }
    }
}
