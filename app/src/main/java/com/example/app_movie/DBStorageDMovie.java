package com.example.app_movie;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;
import java.io.File;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DBStorageDMovie {
   Context context;
   String dbName="DownloadMovie";
   public static String  splitSeparator="__,__";
   public DBStorageDMovie(Context context){
       this.context=context;
   }
   private SQLiteDatabase openDB(){
       return context.openOrCreateDatabase(dbName, Context.MODE_PRIVATE, null);
   }
   public void closeDB(SQLiteDatabase db){
       db.close();
   }
   public void createTable(){
       SQLiteDatabase db=openDB();
       String sqlFavorite = "create TABLE if not exists "+ dbName+ " (" +
               "ID integer not null primary key autoincrement, " +
               "Name text, " +
               "Director Text, " +
               "Actor Text, " +
               "Genre text, "+
               "Description Text, "+
               "UriLocal Text, "+
               "ImageUrl Text, " +
               "FilmUrl Text);";
       db.execSQL(sqlFavorite);
       closeDB(db);
   }
   public void insertDownloadInfo(FilmClass filmClass,File getUriImage,File getUriVideo,File uriLocal){
       ContentValues contentValues=new ContentValues();
       contentValues.put("Name",filmClass.getName());
       contentValues.put("Director",convertArrayToString(filmClass.getDirector()));
       contentValues.put("Actor",convertArrayToString(filmClass.getActor()));
       contentValues.put("Genre",convertArrayToString(filmClass.getGenre()));
       contentValues.put("Description",filmClass.getDescription());
       contentValues.put("UriLocal",uriLocal.getPath());
       contentValues.put("ImageUrl",getUriImage.getPath());
       contentValues.put("FilmUrl",getUriVideo.getPath());
       SQLiteDatabase db=openDB();
       db.insert(dbName,null,contentValues);
       closeDB(db);
       Toast.makeText(context,"Movie has been add to Download Category",Toast.LENGTH_SHORT).show();
   }
   public static String convertArrayToString(List<String> string){
       String  str="";
       Log.d("yo", "insertDownloadInfo: "+string);
       for(int i=0;i<string.size();i++)
       {
           str+=string.get(i);
           if(i<string.size()-1){
               str+=splitSeparator;
           }
       }
       return str;
   }
   public static List<String> convertStringToArray(String string){
       List<String> result= Arrays.asList(string.split(splitSeparator));
       return result;
   }
   public ArrayList<FilmClass> getAllMovieList(){
       SQLiteDatabase sqLiteDatabase = openDB();
       ArrayList<FilmClass> filmClassArrayList = new ArrayList<>();
       String sql = "Select * from "+dbName;
       Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
       if (cursor != null) {
           while (cursor.moveToNext()) {
                String name=cursor.getString(1);
                List<String> director=convertStringToArray(cursor.getString(2));
                List<String> actor=convertStringToArray(cursor.getString(3));
                List<String> genre=convertStringToArray(cursor.getString(4));
                String description=cursor.getString(5);
                String uriLocal=cursor.getString(6);
                String imgUrl=cursor.getString(7);
                String videoUrl=cursor.getString(8);
                filmClassArrayList.add(new FilmClass(name,director,actor,genre,description,imgUrl,videoUrl,uriLocal));
           }
       }
       closeDB(sqLiteDatabase);
       return filmClassArrayList;
   }
   public boolean checkExistInDownloadFolder(String name){
       SQLiteDatabase database=openDB();
       String query="Select Name from "+dbName;
       Cursor cursor=database.rawQuery(query,null,null);
       if(cursor!=null){
           Log.d("tag", "checkExistInDownloadFolder: "+name);
           while (cursor.moveToNext()){
               if(name.compareTo(cursor.getString(0))==0){
                   closeDB(database);
                   return true;
               }
           }
       }
       closeDB(database);
       return false;
   }
   public void removeDownload(String name,File Img,File Video){
       SQLiteDatabase db=openDB();
           String sql="delete from "+dbName+" where Name='"+name+"'";
           db.execSQL(sql);
           Img.delete();
           Video.delete();
           Toast.makeText(context,"File has been deleted",Toast.LENGTH_SHORT).show();
   }
}
