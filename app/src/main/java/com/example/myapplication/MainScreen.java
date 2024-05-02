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

public class MainScreen extends AppCompatActivity
{

    // Không được xóa những dòng comment
    private FirebaseDatabase db;
    private DatabaseReference ref;

    ImageButton tra_cuu;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_screen);

        // Không được xóa
        db = FirebaseDatabase.getInstance();
        ref = db.getReference();


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_screen), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageButton cauhoi = findViewById(R.id.trang_chu_cau_hoi_button);
        cauhoi.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view)
            {
                Intent cau_hoi_intent = new Intent(MainScreen.this, CauHoiScreen.class);
                startActivity(cau_hoi_intent);
            }

        });

         ImageButton dethi = findViewById(R.id.trang_chu_de_thi_button);
         dethi.setOnClickListener(new View.OnClickListener()
            {
             @Override
             public void onClick(View view)
             {
                 Intent de_thi_intent = new Intent(MainScreen.this, DeThiScreen.class);
                 startActivity(de_thi_intent);
             }
         });

        // Không được xóa
        /*GIANGVIEN admin = new GIANGVIEN("000000", "admin", new Date(946684800000L), "nam","admin","00000");
        ref.child("GIANGVIEN").child(admin.getMaGV()).setValue(admin);*/

        /*DETHI dt_1 = new DETHI("000000", 0, "2324", "01/05/2004","100","10000");
        DETHI dt_2 = new DETHI("000001", 1, "2325", "02/05/2004","101","10001");
        DETHI dt_3 = new DETHI("000002", 2, "2326", "03/05/2004","102","10002");
        DETHI dt_4 = new DETHI("000003", 3, "2327", "04/05/2004","103","10003");
        DETHI dt_5 = new DETHI("000004", 4, "2328", "05/05/2004","104","10004");
        DETHI dt_6 = new DETHI("000005", 5, "2329", "06/05/2004","105","10005");
        DETHI dt_7 = new DETHI("000006", 6, "2330", "07/05/2004","106","10006");
        DETHI dt_8 = new DETHI("000007", 7, "2331", "08/05/2004","107","10007");
        DETHI dt_9 = new DETHI("000008", 8, "2332", "09/05/2004","108","10008");
        DETHI dt_10 = new DETHI("000009", 9, "2333", "10/05/2004","109","10009");
        DETHI dt_11 = new DETHI("000010", 10, "2334", "11/05/2004","110","10010");
        DETHI dt_12 = new DETHI("000011", 11, "2335", "12/05/2004","111","10011");
        DETHI dt_13 = new DETHI("000012", 12, "2336", "13/05/2004","112","10012");
        DETHI dt_14 = new DETHI("000013", 13, "2337", "14/05/2004","113","10013");
        DETHI dt_15 = new DETHI("000014", 14, "2338", "15/05/2004","114","10014");
        DETHI dt_16 = new DETHI("000015", 15, "2339", "16/05/2004","115","10015");
        DETHI dt_17 = new DETHI("000016", 16, "2340", "17/05/2004","116","10016");
        DETHI dt_18 = new DETHI("000017", 17, "2341", "18/05/2004","117","10017");
        DETHI dt_19 = new DETHI("000018", 18, "2342", "19/05/2004","118","10018");
        DETHI dt_20 = new DETHI("000019", 19, "2343", "20/05/2004","119","10019");
        DETHI dt_21 = new DETHI("000020", 20, "2344", "21/05/2004","120","10020");

        ref.child("DETHI").child(dt_1.getMaGV()).setValue(dt_1);
        ref.child("DETHI").child(dt_2.getMaGV()).setValue(dt_2);
        ref.child("DETHI").child(dt_3.getMaGV()).setValue(dt_3);
        ref.child("DETHI").child(dt_4.getMaGV()).setValue(dt_4);
        ref.child("DETHI").child(dt_5.getMaGV()).setValue(dt_5);
        ref.child("DETHI").child(dt_6.getMaGV()).setValue(dt_6);
        ref.child("DETHI").child(dt_7.getMaGV()).setValue(dt_7);
        ref.child("DETHI").child(dt_8.getMaGV()).setValue(dt_8);
        ref.child("DETHI").child(dt_9.getMaGV()).setValue(dt_9);
        ref.child("DETHI").child(dt_10.getMaGV()).setValue(dt_10);
        ref.child("DETHI").child(dt_11.getMaGV()).setValue(dt_11);
        ref.child("DETHI").child(dt_12.getMaGV()).setValue(dt_12);
        ref.child("DETHI").child(dt_13.getMaGV()).setValue(dt_13);
        ref.child("DETHI").child(dt_14.getMaGV()).setValue(dt_14);
        ref.child("DETHI").child(dt_15.getMaGV()).setValue(dt_15);
        ref.child("DETHI").child(dt_16.getMaGV()).setValue(dt_16);
        ref.child("DETHI").child(dt_17.getMaGV()).setValue(dt_17);
        ref.child("DETHI").child(dt_18.getMaGV()).setValue(dt_18);
        ref.child("DETHI").child(dt_19.getMaGV()).setValue(dt_19);
        ref.child("DETHI").child(dt_20.getMaGV()).setValue(dt_20);*/

        tra_cuu = findViewById(R.id.trang_chu_tra_cuu_button);
        tra_cuu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainScreen.this, TraCuuScreen.class));
            }
        });
    }
}
