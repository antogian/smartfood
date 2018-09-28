package com.athena.dao;

import com.athena.entities.Category;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.lang.reflect.Type;
import java.util.List;

@Repository
public class CategoryDAO
{

    public CategoryDAO()
    {
    }

    public List<Category> getAllCategories() throws FileNotFoundException
    {
//        Type REVIEW_TYPE = new TypeToken<List<Category>>() {}.getType();
//
//        Gson gson = new Gson();
//
//        ClassLoader classLoader = getClass().getClassLoader();
//        //TODO: Null Pointer exception needs handling
//        File file = new File(classLoader.getResource("data/data.json").getFile());
//
//        JsonReader reader = new JsonReader(new FileReader(file.getPath()));
//        List<Category> allCategories = gson.fromJson(reader, new TypeToken<List<Category>>(){}.getType());
//
//        return allCategories;

        Gson gson = new Gson();
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream in = classLoader.getResourceAsStream("data/data.json");
        JsonReader reader = new JsonReader(new InputStreamReader(in));
        List<Category> allCategories = gson.fromJson(reader, new TypeToken<List<Category>>(){}.getType());

        return allCategories;
    }

}