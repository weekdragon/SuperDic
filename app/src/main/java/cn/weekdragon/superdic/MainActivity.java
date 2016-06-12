package cn.weekdragon.superdic;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.weekdragon.superdic.fragments.NoteFragment;
import cn.weekdragon.superdic.fragments.SerchFragment;
import cn.weekdragon.superdic.fragments.WordBankFragment;
import cn.weekdragon.superdic.views.ListenClipboardService;
import cn.weekdragon.superdic.views.ViewPagerIndicator;


public class MainActivity extends FragmentActivity {
    private List<String> mTitle = Arrays.asList("翻译", "词库", "生词本");
    private List<Fragment> views = new ArrayList<>();
    private ViewPagerIndicator indicator;
    private ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListenClipboardService.start(this);
        //判断当前SDK版本号，如果是4.4以上，就是支持沉浸式状态栏的
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        initData();
        initView();

        indicator.setmVisible_Count(3);
        indicator.setTabItemTitles(mTitle);
        indicator.setViewPager(viewPager, 0);
    }

    private void initData() {
        views.add(new SerchFragment());
        views.add(new WordBankFragment());
        views.add(new NoteFragment());
    }

    private void initView() {
        indicator = (ViewPagerIndicator) findViewById(R.id.indicator);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return views.get(position);
            }

            @Override
            public int getCount() {
                return views.size();
            }
        });
    }
}
