package edu.upc.mcia.publications.ui.publication_details

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import edu.upc.mcia.publications.R
import edu.upc.mcia.publications.databinding.ActivityPublicationDetailsBinding

class PublicationDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPublicationDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_publication_details)

        if (supportFragmentManager.findFragmentById(R.id.container) == null) {
            supportFragmentManager.beginTransaction()
                    .add(R.id.container, PublicationDetailsFragment.newInstance(unpackPublicationId(intent)))
                    .commit()
        }
    }

    companion object {

        private const val EXTRA_PUBLICATION_ID = "extra_publication_id"

        fun unpackPublicationId(intent: Intent): String = intent.getStringExtra(EXTRA_PUBLICATION_ID)

        fun getIntent(context: Context, publicationId: String): Intent {
            return Intent(context, PublicationDetailsActivity::class.java).apply {
                putExtra(EXTRA_PUBLICATION_ID, publicationId)
            }
        }
    }
}