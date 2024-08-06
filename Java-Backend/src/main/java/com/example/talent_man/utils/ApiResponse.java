package com.example.talent_man.utils;

import com.example.talent_man.models.Role;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data

@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class ApiResponse<T> implements Serializable {


    private T item;
    private List<T> items; // List of items
    private int status;
    private String message;

    public ApiResponse(int stat, String mess){
        this.status = stat;
        this.message = mess;

    }

    public ApiResponse(){

    }

    public  ApiResponse(Object Payload, String mess, int status) {
        this.status = status;
        this.message = mess;
        this.item = (T) Payload;

    }
}
