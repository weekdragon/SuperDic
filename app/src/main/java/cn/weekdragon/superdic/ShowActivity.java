package cn.weekdragon.superdic;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import cn.weekdragon.superdic.bean.Const;
import cn.weekdragon.superdic.bean.PosAndAcc;
import cn.weekdragon.superdic.bean.PsAndPron;
import cn.weekdragon.superdic.bean.Sent;
import cn.weekdragon.superdic.bean.Word;
import cn.weekdragon.superdic.bean.WordInfo;
import cn.weekdragon.superdic.db.WordDao;
import cn.weekdragon.superdic.util.Http;

/**
 * 词性用ListView来显示
 *
 * @author WeekDragon
 */
public class ShowActivity extends Activity implements OnClickListener {

    private WordDao dao;
    // 返回
    private ImageView imageBack;
    //添加到生词本
    private Button addToNote;
    // 显示查询单词名
    private TextView wordtext;
    // 英式发音
    private ImageView imageEN;
    // 美式发音
    private ImageView imageUS;
    //	// 英式 音标
//	private TextView textEN;
//	// 美式音标
//	private TextView textUS;
    // 英式音标
    private TextView textEnYinBiao;
    // 美式音标
    private TextView textUSYinBiao;

    // 定义英式音频mp3网络地址
    private String enUrl;
    // 定义美式音频mp3网络地址
    private String usUrl;
    // 基本译义
    private TextView textYiYi;
    // 例句分析
    private TextView textLiJu;
    // 数组适配器
    private ArrayAdapter<String> arrayAdapter;

    // 创建Word对象
    private Word word;
    private ListView listView;
    private ListView listView2;
    // 自定义适配器
    private MyAdapter adapter;
    // 创建
    // 创建PosAndAcc集合
    private List<PosAndAcc> posAndAccs;
    private PosAndAcc acc;
    private int size = 0;

    // 创建sent集合
    private SentAdapter sendAdapter;
    private List<Sent> sentlList;
    private Sent sent;
    private int sentSize = 0;

    // 定义PsAndPron
    List<PsAndPron> psAndPronslList;
    PsAndPron psAndPron;

    // 定义下载XML文件路径
    private String path;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.show_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        dao = new WordDao(this);
        handler = new Handler();
        addToNote = (Button) findViewById(R.id.addToNote);
        imageBack = (ImageView) findViewById(R.id.back);// 返回
        wordtext = (TextView) findViewById(R.id.wordtext);
        imageEN = (ImageView) findViewById(R.id.fayin1);
        imageUS = (ImageView) findViewById(R.id.fayin2);
        textEnYinBiao = (TextView) findViewById(R.id.textenfayin);
        textUSYinBiao = (TextView) findViewById(R.id.textusfayin);
//		textEN = (TextView) findViewById(R.id.texten);
//		textUS = (TextView) findViewById(R.id.textus);
        textYiYi = (TextView) findViewById(R.id.ciyitext);
        textLiJu = (TextView) findViewById(R.id.textView6);
        listView = (ListView) findViewById(R.id.listView1);
        adapter = new MyAdapter();
        listView.setAdapter(adapter);
        // adapter.notifyDataSetChanged();
        /** 将Sent集合数据显示在ListView上 **/
        listView2 = (ListView) findViewById(R.id.listView2);
        sendAdapter = new SentAdapter();
        listView2.setAdapter(sendAdapter);
        // sendAdapter.notifyDataSetChanged();
        // 每次在MainActivity文本框中输入的Word对象赋值后，在此调用
        word = Const.word;
        if (word != null) {
            posAndAccs = word.getPosandacc();
            size = posAndAccs.size();
            sentlList = word.getSent();
            sentSize = sentlList.size();
            // 将Word对象的属性显示在ShowActivity上
            showWordInfo(word);
        }
        /** 点击事件处理 **/
        // 返回的點擊事件
        imageBack.setOnClickListener(this);
        addToNote.setOnClickListener(this);
        // 判断解析出来发音的个数

        int psSize = word.getPsandpron().size();
        if (psSize == 1) {
            imageEN.setOnClickListener(this);
        } else if (psSize == 2) {
            imageEN.setOnClickListener(this);
            imageUS.setOnClickListener(this);
        }

    }

    // 启动线程
    public class MyThread implements Runnable {
        @Override
        public void run() {
            try {
                // 获得下载并解析的Word对象
                word = Http.getHttp(path);
                // 将下载获取的Word对象赋值给Const类中word的引用
                if (word != null) {
                    Const.word = word;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    System.out.println("--ShowActivity--runnable--"
                            + word.getKey());
                    Intent intent = new Intent(ShowActivity.this,
                            ShowActivity.class);
                    startActivity(intent);
                    finish();
                }
            };
            handler.post(r);
        }
    }

    private void showWordInfo(Word word) {
        // 显示单词名称
        wordtext.setText(word.getKey());
        // 给文本框默认显示查询的单词
        psAndPronslList = word.getPsandpron();
        // 判断能否获取音标，如果能获取到，就赋值，获取不到就为null
        if (psAndPronslList.size() == 1) {
            textEnYinBiao.setText(word.getPsandpron().get(0).getPs());
            textUSYinBiao.setText("空");
        } else if (psAndPronslList.size() == 2) {
            textEnYinBiao.setText(word.getPsandpron().get(0).getPs());
            textUSYinBiao.setText(word.getPsandpron().get(1).getPs());
        } else {
            textEnYinBiao.setText("空");
            textUSYinBiao.setText("空");
        }
    }

    @Override
    public void onClick(View arg0) {

        switch (arg0.getId()) {
            //加入生词本
            case R.id.addToNote:
                word.getKey();

                WordInfo info = new WordInfo(word.getKey(), word.getPosandacc().toString());
                dao.add(info);
                break;
            // 返回
            case R.id.back:
                this.finish();
                break;
            // 英式发音
            case R.id.fayin1:
                String eNuriString = word.getPsandpron().get(0).getPron();
                try {
                    Uri enurl = Uri.parse(eNuriString);
                    MediaPlayer mPlayerEn = new MediaPlayer();
                    mPlayerEn.setDataSource(this, enurl);
                    mPlayerEn.prepare();
                    mPlayerEn.start();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                break;
            // 美式发音
            case R.id.fayin2:
                String uSuriString = word.getPsandpron().get(1).getPron();
                try {
                    Uri usurl = Uri.parse(uSuriString);
                    MediaPlayer mPlayerUs = new MediaPlayer();
                    mPlayerUs.setDataSource(this, usurl);
                    mPlayerUs.prepare();
                    mPlayerUs.start();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                break;

            default:
                break;
        }
    }

    /**
     * 基本译义ListView
     **/
    public class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return size;
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return posAndAccs.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return arg0;
        }

        @Override
        public View getView(int arg0, View arg1, ViewGroup arg2) {
            View view = LayoutInflater.from(ShowActivity.this).inflate(
                    R.layout.list_cixing_item, null);
            TextView textviewcixing = (TextView) view
                    .findViewById(R.id.textviewcixing);
            TextView cixingjieshi = (TextView) view
                    .findViewById(R.id.cixingjieshi);
            acc = posAndAccs.get(arg0);
            textviewcixing.setText(acc.getPos());
            cixingjieshi.setText(acc.getAcceptation());
            cixingjieshi.setSelected(true);
            return view;
        }
    }

    /**
     * 例句分析ListView
     **/
    public class SentAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return sentSize;
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return sentlList.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return arg0;
        }

        @Override
        public View getView(int arg0, View arg1, ViewGroup arg2) {
            View view = LayoutInflater.from(ShowActivity.this).inflate(
                    R.layout.list_liju_item, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.biaoqian);
            TextView liju = (TextView) view.findViewById(R.id.liju);
            sent = sentlList.get(arg0);
            StringBuilder builder = new StringBuilder();
            builder.append(sent.getOrig());
            builder.append(sent.getTrans());
            String lijuInfo = builder.toString();
            liju.setText(lijuInfo);
            return view;
        }
    }
}
