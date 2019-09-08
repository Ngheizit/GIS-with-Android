package com.example.pc_08.myapplication;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.security.PublicKey;
import java.util.Random;

public class NoteActivity extends AppCompatActivity {


    private ImageView img_family;
    private TextView tv_content;
    private EditText et_answer;
    private Button btn_check;
    private Button btn_defeated;
    private Question question;

    private Question[] questionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        // 透明化图片
        img_family = (ImageView) findViewById(R.id.family);
        img_family.setAlpha(100);

        // 初始化问题集
        this.AddQuestionsToList();

        tv_content = (TextView)findViewById(R.id.tv_content);
        // 随机生成题目
        question = this.GetQuestionFromRandom();
        tv_content.setText(question.content);

        btn_check = (Button)findViewById(R.id.btn_check);
        btn_defeated = (Button)findViewById(R.id.btn_defeated);
        btn_check.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                et_answer = (EditText)findViewById(R.id.et_answer);
                if(question.check(et_answer.getText().toString())){
                    Toast.makeText(getApplicationContext(), "Good *^_^*", Toast.LENGTH_SHORT).show();
                    question = GetQuestionFromRandom();
                    tv_content.setText(question.content);
                    et_answer.setText("");
                }else {
                    Toast.makeText(getApplicationContext(), "Too Bad ::>_<::", Toast.LENGTH_LONG).show();
                }
            }
        });
        btn_defeated.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String show = question.right;
                Toast.makeText(getApplicationContext(), show, Toast.LENGTH_LONG).show();
            }
        });

    }

    private void AddQuestionsToList(){
        questionList = new Question[]{

                // ----- 全球定位系统应用
                new Question("卫星定位技术是利用人造地球卫星进行_____的技术。", "点位测量"),
                new Question("早期卫星定位技术把人造地球卫星当作_____。", "空间观测目标"),
                new Question("卫星多普勒定位使卫星定位技术发展到了把卫星作为_____的高级阶段。", "动态已知点"),
                new Question("GPS卫星颗数", "21+3"),
                new Question("GPS卫星轨道面个数", "6"),
                new Question("GPS卫星高度_____km", "20200"),
                new Question("GPS卫星运行周期", "11h58min，12恒星时"),
                new Question("GPS卫星轨道倾角_____°", "55"),
                new Question("GPS现代化包括_____和_____部分", "军事，民用"),



                // ----- 计量地理学
                new Question("计量地理学的研究对象", "空间与过程，生态，区域综合体"),
                new Question("计量地理学中空间研究的内容", "空间规律性，空间过程"),
                new Question("计量地理学的理论体系", "地理事物的空间分布规律性，地理事物的空间构成，地理事物的空间过程，地理系统的预测与规划"),
                new Question("计量地理学的研究方法", "演绎"),
                new Question("计量地理学研究的具体方法", "地理系统分析，随机数学方法的应用，地理系统模拟，电子计算机的应用"),




                // ----- 地理数据库技术
                new Question("地理数据库的4个基本概念", "数据，数据库，数据库管理系统，数据库系统"),
                new Question("数据是____中存储的____", "数据库，基本对象"),
                new Question("数据的定义", "描述事物的符号记录"),
                new Question("数据库的定义", "是长期存储在计算机内、由组织的、可共享的大量数据的集合"),
                new Question("数据库的基本特征", "数据按一定的数据模型组织、描述和存储，可为各种用户共享，冗余度较小，数据独立性较高，易拓展"),
                new Question("数据库管理系统", "位于用户与操作系统之间的一层数据管理软件，是基础软件，是一个大型复杂的软件系统"),
                new Question("数据库管理系统的用途", "科学地组织和存储数据、高效地获取和维护数据"),
                new Question("数据库管理系统的主要功能", "数据定义功能，数据组织、存储和管理，数据操作功能，数据的事务管理和运行管理，数据库的建立和维护功能"),
                new Question("数据库系统的构成", "数据库，数据库管理系统，应用程序，数据库管理员"),
                new Question("数据管理的定义", "对数据进行分类、组织、编码、存储、检索和维护，是数据处理的中心问题"),
                new Question("数据管理技术的发展过程", "人工管理阶段，文件系统阶段，数据库系统阶段"),

//                new Question("", ""),
//                new Question("", ""),
//                new Question("", ""),
//                new Question("", ""),
//                new Question("", ""),
//                new Question("", ""),
//                new Question("", ""),
//                new Question("", ""),
//                new Question("", ""),
//                new Question("", ""),
//                new Question("", ""),
//                new Question("", ""),
//                new Question("", ""),
//                new Question("", ""),
//                new Question("", ""),
//                new Question("", ""),
//                new Question("", ""),
//                new Question("", ""),
//                new Question("", ""),
//                new Question("", ""),
//                new Question("", ""),
//                new Question("", ""),
//                new Question("", ""),
//                new Question("", ""),
//                new Question("", ""),
//                new Question("", ""),
//                new Question("", ""),
//                new Question("", ""),
//                new Question("", ""),
//                new Question("", ""),
//                new Question("", ""),
//                new Question("", ""),
//                new Question("", ""),
//                new Question("", ""),
//                new Question("", ""),
//                new Question("", ""),
//                new Question("", "")
        };
    }

    private Question GetQuestionFromRandom(){
        Random r = new Random();
        int i = r.nextInt(this.questionList.length);
        return this.questionList[i];
    }



}

