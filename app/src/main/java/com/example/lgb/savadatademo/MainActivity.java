package com.example.lgb.savadatademo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    String SDPATH = Environment.getExternalStorageDirectory() + "/";
    private File dir;
    private TextView tv;//显示数据用

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewByID();
        initView();


    }

    private void initView() {

        FileSDSaveDemo();
       // FileSaveDemo();
    }

    private void findViewByID() {
        tv = (TextView) findViewById(R.id.tv1);
    }


    /*
    * Context.MODE_PRIVATE：为默认操作模式,代表该文件是私有数据,只能被应用本身访问,在该模式下,写入的内容会覆盖原文件的内容
    Context.MODE_APPEND：模式会检查文件是否存在,存在就往文件追加内容,否则就创建新文件.
    Context.MODE_WORLD_READABLE和Context.MODE_WORLD_WRITEABLE用来控制其他应用是否有权限读写该文件.
    MODE_WORLD_READABLE：表示当前文件可以被其他应用读取.
    MODE_WORLD_WRITEABLE：表示当前文件可以被其他应用写入.*/

    //sharepreences的demo
    private void SharePreferencesDemo() {
         /*
        * 这种方式仅能在当前activity中使用*/
        SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();

        editor.putString("lgb", "123");
        editor.putInt("abc", 1);
        editor.commit();

        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        String a = sharedPreferences.getString("lgb", "abc");
        int b = sharedPreferences.getInt("abc", 2);
        tv.setText(a + b);
        //这种方式是根据context获取数据，可以在整个应用中使用
        SharedPreferences preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor2 = preferences.edit();
        editor2.putString("name", "lgb");
        editor2.putInt("key", 3);
        editor2.commit();
        SharedPreferences sharedPreferences2 = getSharedPreferences("user", MODE_PRIVATE);
        String name = sharedPreferences2.getString("name", "误解");
        int key = sharedPreferences2.getInt("key", 0);
        tv.append(name + key);
    }


    //文件存储（SD卡存储）
    private void FileSDSaveDemo() {
        //获取文件夹
        File dir = new File(SDPATH + "/" + "text");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dir.getPath() + "//" + "lgb.txt");
        System.out.println(file.getName());
        try {
            if (!file.exists()) {
                System.out.println("不存在");
                file.createNewFile();
            }
            writefileSD(file);
            tv.setText(readfileSD(file).toString());
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    //SD卡文件读取
    private String readfileSD(File file) {
        try {
            FileInputStream inStream = new FileInputStream(file);//实例化文件输入流对象
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];//定义缓存数组
            int length = -1;
            while ((length = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, length);
            }
            outStream.close();
            inStream.close();
            return outStream.toString();
        } catch (IOException e) {

            return null;
        }
    }

    //SD卡文件存储写入
    private void writefileSD(File file) {
        try {
            FileOutputStream out = new FileOutputStream(file, true);//实例化文件输出流对象，不存在则自动新建
            String bb = "ABC";
            out.write(bb.getBytes());
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /*在使用openFileOutput方法打开文件以写入数据时，需要指定打开模式。默认为零，即MODE_PRIVATE。

不同的模式对应的的含义如下：
   常量                                          含义
MODE_PRIVATE                  默认模式，文件只可以被调用该方法的应用程序访问
MODE_APPEND                   如果文件已存在就向该文件的末尾继续写入数据，而不是覆盖原来的数据。
MODE_WORLD_READABLE           赋予所有的应用程序对该文件读的权限。
MODE_WORLD_WRITEABLE          赋予所有的应用程序对该文件写的权限。 */
    //一般文件存储
    private void FileSaveDemo() {
            try {
                String dirPath = getFilesDir().getParent()+File.separator+".dir";
                File dir = new File(dirPath);
                dir.mkdirs();

                //设置权限  该文件夹以及其子文件
                String str = "chmod " +dirPath+" "+"777"+" && busybox chmod"+dirPath +" "+"777";
                //执行执行权限指令
                Runtime.getRuntime().exec(str);
                File file = new File(dirPath+File.separator+"lgb.txt");
                if(!file.exists()) {
                    file.createNewFile();
                    System.out.println("123");
                }
                writeFile(file);
               tv.setText(readFile(file));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//系统文件读取，打开系统文件
    private String readFile(File file){
        try {
            FileInputStream fis = openFileInput(file.getName());

            byte [] buffers = new byte[fis.available()];
            fis.read(buffers);
            fis.close();
            return new String(buffers);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }
    //文件写入，此例子这里为文件叠加型
    private void writeFile(File file){
        try {
            FileOutputStream fos = openFileOutput(file.getName(),MODE_APPEND);
            fos.write("liguboibao".getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
