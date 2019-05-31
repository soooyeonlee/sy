package com.example.ip_inventory;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;




import java.util.HashMap;

public class EditActivity extends AppCompatActivity {

    private EditText mId, mName, mDesignation, mSalary;
    private Button mBtnAdd;
    //private String mId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        // Initialize EditText View
        mId = (EditText) findViewById(R.id.id);
        mName = (EditText) findViewById(R.id.name);
        mDesignation = (EditText) findViewById(R.id.designation);
        mSalary = (EditText) findViewById(R.id.salary);
        mBtnAdd = (Button) findViewById(R.id.btn_add);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null){
            mId.setText(bundle.getString("ID"));
            mName.setText(bundle.getString("NAME"));
            mDesignation.setText(bundle.getString("DESIGNATION"));
            mSalary.setText(bundle.getString("SALARY"));
        }
    }

    public void updateEmployee(View view) {
        String id = mId.getText().toString();
        String name = mName.getText().toString();
        String designation = mDesignation.getText().toString();
        String salary = mSalary.getText().toString();

        HashMap<String, String> requestedParams = new HashMap<>();
        requestedParams.put("id", id);
        requestedParams.put("name", name);
        requestedParams.put("designation", designation);
        requestedParams.put("salary", salary);
        Log.d("HashMap", requestedParams.get("id"));
        Toast.makeText(getApplicationContext(), "Success!! Employee Updated ID : " + requestedParams.get("id"), Toast.LENGTH_LONG).show();

        PostRequestHandler postRequestHandler = new PostRequestHandler(Constant.UPDATE, requestedParams);
        postRequestHandler.execute();

        listEmployee(view);
    }

    public void deleteEmployee(View view) {
        String id = mId.getText().toString();
//        String name = mName.getText().toString();
//        String designation = mDesignation.getText().toString();
//        String salary = mSalary.getText().toString();

        HashMap<String, String> requestedParams = new HashMap<>();
        requestedParams.put("id", id);
//        requestedParams.put("name", name);
//        requestedParams.put("designation", designation);
//        requestedParams.put("salary", salary);
        Log.d("HashMap", requestedParams.get("id"));
        Toast.makeText(getApplicationContext(), "Success!! Employee Deleted ID : " + requestedParams.get("id"), Toast.LENGTH_LONG).show();


        PostRequestHandler postRequestHandler = new PostRequestHandler(Constant.DELETE, requestedParams);
        postRequestHandler.execute();

        listEmployee(view);
    }

    public void listEmployee(View view) {
        Intent intent = new Intent(EditActivity.this, ViewActivity.class);
        startActivity(intent);
    }

}


