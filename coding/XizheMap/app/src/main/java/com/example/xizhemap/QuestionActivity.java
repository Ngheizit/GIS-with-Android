package com.example.xizhemap;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;

public class QuestionActivity extends AppCompatActivity {

    private void AddQuestionsToList() {
        questionList = new Question[]{

                // ----- 全球定位系统应用
                new Question("卫星定位技术是利用人造地球卫星进行_____的技术。", "点位测量"),
                new Question("早期卫星定位技术把人造地球卫星当作_____。", "空间观测目标"),
                new Question("卫星多普勒定位使卫星定位技术发展到了把卫星作为_____的高级阶段。", "动态已知点"),
                new Question("美国的全球定位系统", "GPS"),
                new Question("俄罗斯（俄国）的全球导航系统", "GLONASS"),
                new Question("欧盟的全球卫星导航系统", "GAILEO"),
                new Question("北斗卫星导航试验系统是北斗____号，是___导航,利用____卫星，实现_____定位", "一，区域，地球同步，双星"),
                new Question("GPS系统包含三大部分，空间部分：___，地面控制部分：___，用户设备部分：___", "GPS卫星星座，地面控制系统，GPS信号接收机"),
                new Question("GPS卫星星座由___颗工作卫星和___颗在轨备用卫星组成", "21，3"),
                new Question("GPS的___颗卫星___分布在___个轨道品面内，轨道倾角为___°，各轨道平面之间相距___°，一轨道平面上的卫星比西边相邻轨道平面上的相应卫星超前___度", "24，均匀，6，55，60，30"),
                new Question("在___km高空的GPS卫星，绕地球一周的时间为___恒星时，对于地面观测者来说，每天将提前___min见到同一颗GPS卫星", "20200，12，4"),
                new Question("位于地平面以上的卫星颗数随时间和地点的不同而不同，最少可见到___颗，最多可见到___颗", "4，11"),
                new Question("在用GPS信号导航定位时，为了解算测站的三维坐标，必须观测___颗GPS卫星，称为___", "4，定位星座"),
                new Question("在GPS系统中，GPS卫星的作用：①用___波段的两个无线载波向广大用户___地发送___信号；②在卫星飞越___上空时，接受由地面___用___波段发送到卫星的导航电文和其他有关信息，并通过GPS信号电路适时地发送到广大用户；③接受地面___通过___发送到卫星的___命令，适时地改正运行偏差或启用备用时钟等","L，连续不断，导航定位，注入站，注入站，S，主控站，注入站，调度"),
                new Question("GPS工作卫星的地面监控系统由___个，包含___个主控站，___个注入站，___个监控站", "5，1，3，5"),

                // ----- 计量地理学
                    //  ----- 第二周
                new Question("计量地理学的定义", "是将数学和电子计算机技术应用于地理学的一门新兴学科"),
                new Question("地理数据在建模分析中的作用：①确定地理模型中的___与___；②检验模型的___、___和___", "参数，初值，正确性，合理性，有效性"),
                new Question("地理数据类型：①___数据和___数据；②___数据和___数据", "定性，定量，空间，属性"),
                new Question("地理数据特征", "形式化数量化，多时空尺度，多维性，不确定性"),
                new Question("___化、___化与___化是所有地理数据的共同特征", "形式，逻辑，数量"),
                new Question("地理数据不确定性的来源：①___不确定性，数据源不确定性、数据处理；②___不确定性，定义模糊、数据源不确定性；③___不确定性，语义表达不确定性；④___不确定性，参数不确定性，模型传播", "位置，属性，时域，模型"),
                new Question("统计分组的定义", "按某一标志划分为若干性质不同但又有联系的几个部分"),
                new Question("统计分组根据分组标志，分为按___标志分组和按___标志分组", "品质，数量"),
                new Question("地理数据统计处理常用的统计指标", "集中趋势，离散程度，分布特征"),
                new Question("变异系数表示地理数据的___、___程度", "相对变化，波动"),
                new Question("偏度系数测度地理数据分布的___情况，刻画以___为中心的___情况", "不对称性，平均数，偏向"),
                new Question("峰度系数测度地理数据在___附近的___程度", "峰度，集中"),
                    // ----- 第三周
                new Question("空间布局的测度包括___布局，___布局，___区域，___区域", "点状，线状，离散，连续"),
                new Question("空间分布的测度包括___的测度，对___的测度，___程度的测度", "最邻近平均距离，中心位置，离散"),
                new Question("最邻近平均距离的测度包括___法、___法","顺序，区域"),
                new Question("中心位置的测度包括___，___和___","中项中心，平均中心，区域重心"),
                new Question("离散程度的测度包括对___、___的离散程度，对___的离散程度和___的离散程度", "平均中心，中项中心，任意指定中心，各点之间")




        };
    }

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
        setContentView(R.layout.activity_question);

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

    private Question GetQuestionFromRandom(){
        Random r = new Random();
        int i = r.nextInt(this.questionList.length);
        return this.questionList[i];
    }
}
