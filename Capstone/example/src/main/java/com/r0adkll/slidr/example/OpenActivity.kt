package com.r0adkll.slidr.example

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.DialogFragment

class OpenActivity : AppCompatActivity() {
    private var selectedIndex: Int = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val layout = intent.getIntExtra("layout_file_id", R.layout.demo1)
        setContentView(layout)

        val motionLayout = findViewById<MotionLayout>(R.id.motion_container)

        val v1 = findViewById<View>(R.id.v1)
        val v2 = findViewById<View>(R.id.v2)
        val v3 = findViewById<View>(R.id.v3)

        val imageview_1 = findViewById<ImageView>(R.id.start_image_1)
        val imageview_2 = findViewById<ImageView>(R.id.start_image_2)
        val imageview_3 = findViewById<ImageView>(R.id.start_image_3)
        imageview_1.setColorFilter(Color.parseColor("#FFFFFFFF"))
        imageview_2.setColorFilter(Color.parseColor("#FFFFFFFF"))
        imageview_3.setColorFilter(Color.parseColor("#FFFFFFFF"))

        v1.setOnClickListener {
            if (selectedIndex == 0) {
                startActivity(Intent(this@OpenActivity,MainActivity::class.java));
                return@setOnClickListener
            }
            motionLayout.setTransition(R.id.s2, R.id.s1) //orange to blue transition
            motionLayout.transitionToEnd()
            selectedIndex = 0;
        }
        v2.setOnClickListener {
            if (selectedIndex == 1) {
                startActivity(Intent(this@OpenActivity,TextListActivity::class.java));
                return@setOnClickListener
            }
            if (selectedIndex == 2) {
                motionLayout.setTransition(R.id.s3, R.id.s2)  //red to orange transition
            } else {
                motionLayout.setTransition(R.id.s1, R.id.s2) //blue to orange transition
            }
            motionLayout.transitionToEnd()
            selectedIndex = 1;
        }
        v3.setOnClickListener {
            if (selectedIndex == 2) {
                val builder = AlertDialog.Builder(this@OpenActivity)
                builder.setMessage("정말 종료하시겠습니까?")
                        .setPositiveButton("종료") { dialog, id ->
                            ActivityCompat.finishAffinity(this);
                            System.exit(0);
                        }
                        .setNegativeButton("취소") { dialog, id -> }
                val Exitdialog = builder.create()
                Exitdialog.show()
                return@setOnClickListener
            }
            motionLayout.setTransition(R.id.s2, R.id.s3) //orange to red transition
            motionLayout.transitionToEnd()
            selectedIndex = 2;
        }

    }
}