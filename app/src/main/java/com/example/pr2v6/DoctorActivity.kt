package com.example.pr2v6

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.pr2v6.ui.doc.DoctorFragment

class DoctorActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor)

        var b = getIntent().getExtras()
        var value: Int = -1
        if( b != null )
        {
            value = b.getInt("key")
        }
        else
        {
            error("индекс не передан")
            return
        }

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.doctorFragmentContainerView,
                            DoctorFragment.newInstance(value))
                .commit()
        }

    }
}