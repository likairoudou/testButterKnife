package com.example.testbutterknife;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


/**
 * @Author: lyc
 * @CreateDate: 2021-01-25 13:20
 * @Description: 类描述
 */
@ContentViewInject(R.layout.activity_inject)

public class InJectActivity extends AppCompatActivity {


    @ViewInject(R.id.bt1)
    private Button bt1;
    @ViewInject(R.id.bt2)
    private Button bt2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InjectUtils.ject(this);

        bt1.setText("修改成功");

        bt2.setText("bt2也修改成功了");

    }

    @ClickInject({R.id.bt1,R.id.bt2})
     void myClick(View view){
        if(view.getId()==R.id.bt1){
            Toast.makeText(this,"bt11111==",Toast.LENGTH_LONG).show();
        }else if(view.getId()==R.id.bt2){
            Toast.makeText(this,"bt22222==",Toast.LENGTH_LONG).show();

        }
    }

    @LongClickInject({R.id.bt1,R.id.bt2})
     private boolean myLongClick(View view){
        if(view.getId()==R.id.bt1){
            Toast.makeText(this,"bt11111long==",Toast.LENGTH_LONG).show();
        }else if(view.getId()==R.id.bt2){
            Toast.makeText(this,"bt22222long==",Toast.LENGTH_LONG).show();

        }
        return false;

    }}
