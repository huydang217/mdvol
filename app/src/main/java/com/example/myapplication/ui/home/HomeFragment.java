package com.example.myapplication.ui.home;

import android.icu.util.Calendar;
import android.os.Bundle;
import android.os.Environment;
import android.os.Process;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;

import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.example.dvol.MLib;
import com.example.myapplication.MCONST.MConst;
import com.example.myapplication.databinding.FragmentHomeBinding;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.datepicker.MaterialCalendar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import kotlinx.coroutines.sync.Mutex;
import org.w3c.dom.Text;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private Python py = Python.getInstance();
    private MLib mMLib;
    private boolean compareFiles(File file1, File file2) {
        long lastModified1 = file1.lastModified();
        long lastModified2 = file2.lastModified();
        return lastModified1 == lastModified2;
    }

    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final MaterialTextView textView = binding.txtDebug;
        final TextInputEditText edtUser = binding.editTextUsername;
        final TextInputEditText edtPass = binding.editTextPassword;
        textView.setMovementMethod(new ScrollingMovementMethod());
        final MaterialButton btnTest = binding.buttonTest;
        final MaterialButton btnLogin = binding.buttonLogin;
        // homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        getActivity().getExternalFilesDir(null);
        // hot
//        try {
//            File sourceFile = new File(Environment.getExternalStorageDirectory(), "libdvol.so");
//            File destinationFile = new File(getActivity().getCacheDir(), "libdvol.so");
//
//            if (MLib.copyFile(sourceFile.getAbsolutePath(), destinationFile.getAbsolutePath())) {
//
//                // hot
//
//                // Toast.makeText(getActivity(), libraryPath, Toast.LENGTH_LONG).show();
//                System.load(destinationFile.getAbsolutePath());
//                MLib.MLibInit(getActivity());
//                // Toast.makeText(getActivity(), "ok", Toast.LENGTH_LONG).show();
//            }
//            // Library loaded successfully
//        } catch (Exception e) {
//            e.printStackTrace();
//            // Failed to load the library
//            textView.setText(e.toString());
//        }

        try {
            mMLib = new MLib();
            mMLib.SetPython(Python.getInstance());
            
            PyObject plot = py.getModule("plot");
            plot.callAttr("plot", getActivity(), textView);
        } catch (Exception err) {
            textView.setText(err.getMessage());
        }
        btnLogin.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            PyObject res =
                                    py.getModule(MConst.M_DVOL_LOGINMODULE)
                                            .callAttr(
                                                    MConst.M_DVOL_LOGINMODULE_LOGIN,
                                                    edtUser,
                                                    edtPass);
                            Boolean bRes = res.toBoolean();
                            if (!bRes) {
                                Toast.makeText(getActivity(), "Login Failed!!!", 0).show();
                                return;
                            } else {
                                Toast.makeText(getActivity(),"Login Ok",0).show();
                            }
                        } catch (Exception err) {
                            textView.setText(err.getMessage());
                        }
                    }
                });
        btnTest.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {

                            // TODO Auto-generated method stub

                            PyObject plot = py.getModule("plot");
                            PyObject rl = py.getModule("importlib");
                            rl.callAttr("reload", plot);
                            plot.callAttr("plot", getActivity(), textView);

                        } catch (Exception err) {
                            textView.setText(err.getMessage());
                        }
                    }
                });
        return root;
    }

    public void buttonTestOnClick(View v) {}

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
