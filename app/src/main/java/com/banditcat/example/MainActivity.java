package com.banditcat.example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.banditcat.example.entity.UserEntity;
import com.banditcat.example.model.UserModel;
import com.banditcat.example.model.UserModelMapper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //UserEntity(对应Api pojo)
        UserEntity userEntity = new UserEntity();

        userEntity.setUserId(1000L);
        userEntity.setUserName("cat");
        userEntity.setFullName("banditcat");


        //Activity中，UserModel对应视图模型
        //常规的做法


        UserModel userModel = new UserModel();

        userModel.setStringUserId(userEntity.getUserId().toString());//还需要判断类型
        userModel.setUserName(userEntity.getUserName());
        userModel.setFullName2(userEntity.getFullName());


        //新的做法


        userModel = UserModelMapper.getInstance().transformer(userEntity);//主要是把这些硬编码的方式,通过注解的方式减轻体力

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
