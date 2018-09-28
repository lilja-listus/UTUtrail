package com.example.utu.ututrail;

        import android.content.Intent;
        import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
        import android.view.View;
        import android.widget.Button;

/**
 * Activity that starts the admin activity after the admin logged in
  */

        public class AdminActivity extends AppCompatActivity {

            @Override
    protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_admin);

                Button varataLipun = (Button) findViewById(R.id.varata_lipun);
            varataLipun.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(AdminActivity.this, ChooseDestinationActivity.class);
            startActivity(intent);
        }

    });
            Button changeTrain = (Button) findViewById(R.id.change_train_info);
            changeTrain.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AdminActivity.this, TrainCatalog.class);
                    startActivity(intent);
                }
            });

                Button changeUsers = (Button) findViewById(R.id.change_users);
                changeUsers.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(AdminActivity.this, CatalogActivity.class);
                        startActivity(intent);
                    }
                });
            }
        }