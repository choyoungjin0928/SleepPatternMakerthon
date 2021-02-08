package com.example.dreammaker;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class JoinActivity extends AppCompatActivity {
    EditText etName, etId, etPw, etPwcf, etTel;
    TextView tvName, tvId, tvPw, tvPwcf;
    Button btnJoin, btnBack;
    Boolean pass;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_join);

        etName = (EditText)findViewById(R.id.etJoinName);
        etId = (EditText)findViewById(R.id.etJoinId);
        etPw = (EditText)findViewById(R.id.etJoinPw);
        etPwcf = (EditText)findViewById(R.id.etJoinPwcf);
        etTel = (EditText)findViewById(R.id.etJoinTel);
        tvName = (TextView)findViewById(R.id.tvJoinName);
        tvId = (TextView)findViewById(R.id.tvJoinId);
        tvPw = (TextView)findViewById(R.id.tvJoinPw);
        tvPwcf = (TextView)findViewById(R.id.tvJoinPwcf);
        btnJoin = (Button)findViewById(R.id.btnJoinOK);
        btnBack = (Button)findViewById(R.id.btnJoinBack);

        final AlertDialog.Builder alrB = new AlertDialog.Builder(this);

        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pass = true;
                String id = etId.getText().toString();
                String pw = etPw.getText().toString();
                String pwcf = etPwcf.getText().toString();

                if(!pw.equals(pwcf)) {
                    etPwcf.setTextColor(Color.RED);
                    tvPwcf.setText("⛔ 비밀번호가 다릅니다.");
                    pass = false;
                }
                if(etName.getText().toString().replace(" ", "").equals("")) {
                    etName.setHintTextColor(Color.RED);
                    tvName.setText("⛔ 이름을 입력해주세요");
                    pass = false;
                }
                if(etId.getText().toString().replace(" ", "").equals("")) {
                    etId.setHintTextColor(Color.RED);
                    tvId.setText("⛔ 아이디를 입력해주세요");
                    pass = false;
                }
                if(etPw.getText().toString().replace(" ", "").equals("")) {
                    etPw.setHintTextColor(Color.RED);
                    tvPw.setText("⛔ 비밀번호를 입력해주세요");
                    pass = false;
                }

                if(pass) {
                    final Map<String, Object> user = new HashMap<>();
                    user.put("name", etName.getText().toString());
                    user.put("id", id);
                    String enc = EncryptSHA512.EncryptSHA512(pw);
                    user.put("pw", enc);
                    user.put("phone", etTel.getText().toString());

                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    final CollectionReference collRef = db.collection("users");
                    Query idQuery = collRef.whereEqualTo("id", id);
                    idQuery.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if(queryDocumentSnapshots.isEmpty()) {
                                collRef.add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        alrB.setTitle("회원가입 완료!").setMessage(etName.getText().toString() + "님 환영합니다!");
                                        alrB.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                finish();
                                            }
                                        });
                                        AlertDialog alert = alrB.create();
                                        alert.show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        alrB.setTitle("회원가입 실패").setMessage("서버에 문제가 있습니다. 다시 시도해 보세요");
                                        alrB.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                            }
                                        });
                                        AlertDialog alert = alrB.create();
                                        alert.show();
                                    }
                                });
                            } else {
                                etId.setTextColor(Color.RED);
                                tvId.setText("⛔ 이미 존재하는 아이디 입니다.");
                            }
                        }
                    });
                }

            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void onJoinEditTextClick(View view) {
        EditText et = findViewById(view.getId());
        switch (et.getId()) {
            case R.id.etJoinName :
                et.setHint("이름");
                tvName.setText("");
                break;
            case R.id.etJoinId :
                et.setHint("아이디");
                tvId.setText("");
                break;
            case R.id.etJoinPw :
                et.setHint("비밀번호");
                tvPw.setText("");
                break;
            case R.id.etJoinPwcf :
                et.setHint("비밀번호 확인");
                tvPwcf.setText("");
                break;
        }
        et.setHintTextColor(Color.GRAY);
        et.setTextColor(Color.BLACK);
    }
}
