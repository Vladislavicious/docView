package com.example.pr2v6

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.pr2v6.databinding.ActivityDoctorBinding
import com.example.pr2v6.ui.doc.DoctorFragment


class DoctorActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDoctorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDoctorBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

        setSupportActionBar(binding.DoctorToolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        supportActionBar!!.title = "Информация о враче"

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // handle arrow click here
        if (item.itemId == android.R.id.home) {
            finish() // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item)
    }
}