package com.absurd.circle.ui.adapter;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.absurd.circle.app.R;
import com.absurd.circle.util.FacesUtil;

public class FacesAdapter extends BaseAdapter
{

	private Context	mContext;

	private static int[] mImageIds = new int[]{
            R.drawable.d_aini,R.drawable.d_aoteman, R.drawable.d_baibai, R.drawable.d_beishang, R.drawable.d_bishi, R.drawable.d_bizui, R.drawable.d_chanzui, R.drawable.d_chijing, R.drawable.d_dahaqi, R.drawable.d_ding,
            R.drawable.d_fennu,R.drawable.d_ganmao, R.drawable.d_guzhang, R.drawable.d_haha, R.drawable.d_haixiu, R.drawable.d_han, R.drawable.d_hehe, R.drawable.d_heixian, R.drawable.d_heng, R.drawable.d_huaxin,
            R.drawable.d_keai,R.drawable.d_kelian, R.drawable.d_ku, R.drawable.d_kun, R.drawable.d_landelini, R.drawable.d_lei, R.drawable.d_nanhaier, R.drawable.d_nu, R.drawable.d_numa, R.drawable.d_nvhaier,
            R.drawable.d_qian,R.drawable.d_qinqin, R.drawable.d_shengbing, R.drawable.d_shiwang, R.drawable.d_shuai, R.drawable.d_shudaizi, R.drawable.d_shuijiao, R.drawable.d_sikao, R.drawable.d_taikaixin, R.drawable.d_touxiao, R.drawable.d_tu,
            R.drawable.d_tuzi, R.drawable.d_wabishi, R.drawable.d_weiqu, R.drawable.d_xiongmao, R.drawable.d_xixi, R.drawable.d_xu, R.drawable.d_yinxian, R.drawable.d_yiwen, R.drawable.d_youhengheng, R.drawable.d_yun,
            R.drawable.d_zhuakuang, R.drawable.d_zhutou, R.drawable.d_zuoguilian, R.drawable.d_zuohengheng, R.drawable.f_geili, R.drawable.f_hufen, R.drawable.f_jiong, R.drawable.f_meng, R.drawable.f_shenma, R.drawable.f_shuai,
            R.drawable.f_v5, R.drawable.f_xi, R.drawable.f_zhi, R.drawable.h_buyao, R.drawable.h_good, R.drawable.h_haha, R.drawable.h_lai, R.drawable.h_ok, R.drawable.h_quantou, R.drawable.h_ruo,
            R.drawable.h_woshou, R.drawable.h_ye, R.drawable.h_zan, R.drawable.h_zuicha, R.drawable.l_aixinchuandi, R.drawable.l_shangxin, R.drawable.l_xin, R.drawable.o_bingun, R.drawable.o_dangao, R.drawable.o_dianying,
            R.drawable.o_fahongbao, R.drawable.o_feiji, R.drawable.o_fengshan, R.drawable.o_ganbei, R.drawable.o_hongsidai, R.drawable.o_huatong, R.drawable.o_kafei, R.drawable.o_lazhu, R.drawable.o_liwu, R.drawable.o_lvsidai,
            R.drawable.o_qiche, R.drawable.o_shixi, R.drawable.o_shouji, R.drawable.o_shoutao, R.drawable.o_weibo, R.drawable.o_weiguan, R.drawable.o_wennuanmaozi, R.drawable.o_xigua, R.drawable.o_yinyue, R.drawable.o_zhaoxiangji,
            R.drawable.o_zhong, R.drawable.o_zixingche, R.drawable.o_zuqiu, R.drawable.w_fuyun, R.drawable.w_luoye, R.drawable.w_shachenbao, R.drawable.w_taiyang, R.drawable.w_weifeng, R.drawable.w_xianhua, R.drawable.w_xiayu,
            R.drawable.w_xue, R.drawable.w_xueren, R.drawable.w_yueliang,
    };

	public static int[] getImageIds()
	{
		return mImageIds;
	}
	
	public FacesAdapter(Context c)
	{
		mContext = c;
	}
	
	public int getCount()
	{
		return mImageIds.length;
	}

	public Object getItem(int position)
	{
		return position;
	}


	public long getItemId(int position)
	{
		return mImageIds[position];
	}


	public View getView(int position, View convertView, ViewGroup parent)
	{
		ImageView imageView;
		if (convertView == null){
			imageView = new ImageView(mContext);
			imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
			imageView.setScaleType(ImageView.ScaleType.CENTER);
		}else{
			imageView = (ImageView) convertView;
		}

		imageView.setImageResource(mImageIds[position]);
        imageView.setTag(FacesUtil.tags.get(position) );
		
		return imageView;
	}
}