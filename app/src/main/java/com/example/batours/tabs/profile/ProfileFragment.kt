package com.example.batours.tabs.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.batours.MainActivity
import com.example.batours.R
import com.example.batours.WelcomeActivity
import com.example.batours.databinding.FragmentProfileBinding
import com.example.batours.models.User
import com.example.batours.storage.SharedPrefManager
import com.google.android.material.imageview.ShapeableImageView

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    lateinit var imgAvatar: ShapeableImageView
    lateinit var tvFullName: TextView
    lateinit var tvEmail: TextView
    lateinit var btnLogout: Button

    var user: User? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        user = context?.applicationContext?.let { SharedPrefManager.getInstance(it) }?.user

        imgAvatar = view.findViewById(R.id.img_avatar)
        tvFullName = view.findViewById(R.id.tv_fullName)
        tvEmail = view.findViewById(R.id.tv_email)
        btnLogout = view.findViewById(R.id.btn_logout)

        context?.applicationContext?.let {
            Glide.with(it)
                .load(user?.profile_pic_url)
                .centerCrop()
                .into(imgAvatar)
        }

        tvFullName.text = user?.full_name
        tvEmail.text = user?.email

        btnLogout.setOnClickListener { view ->
            context?.applicationContext?.let { SharedPrefManager.getInstance(it) }?.clear()
            Toast.makeText(context, "Logout succeed", Toast.LENGTH_LONG).show()

            val intent = Intent(context?.applicationContext, WelcomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(intent)
        }
    }
}