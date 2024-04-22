package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.Date;

public class MainScreen extends AppCompatActivity {

    // Không được xóa những dòng comment
    /*private FirebaseDatabase db;
    private DatabaseReference ref;*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_screen);

        // Không được xóa
        /*db = FirebaseDatabase.getInstance();
        ref = db.getReference();*/


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_screen), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        /*ImageButton cauhoi = findViewById(R.id.image_button_4);
        cauhoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent cau_hoi_intent = new Intent(MainScreen.this, CauHoiScreen.class);
                startActivity(cau_hoi_intent);
            }

        });
//         ImageButton dethi = findViewById(R.id.imageButton5);
//         dethi.setOnClickListener(new View.OnClickListener() {
//             @Override
//             public void onClick(View view)
//             {
//                 Intent de_thi_intent = new Intent(MainScreen.this, DeThi.class);
//                 startActivity(de_thi_intent);
//             }
//         });
        });*/
        // Không được xóa
        /*GIANGVIEN admin = new GIANGVIEN("000000", "admin", new Date(946684800000L), "nam","admin","00000");
        ref.child("GIANGVIEN").child(admin.getMaGV()).setValue(admin);*/

    }
}
