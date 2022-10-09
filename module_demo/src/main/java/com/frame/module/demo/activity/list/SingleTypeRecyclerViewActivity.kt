package com.frame.module.demo.activity.list

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import com.frame.basic.base.mvvm.c.vms
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.frame.basic.base.ktx.addDelayChangeAfter
import com.frame.basic.base.ktx.dp
import com.frame.basic.base.ktx.dpF
import com.frame.basic.base.mvvm.c.RecyclerViewBasicPlugin
import com.frame.basic.base.utils.ToastUtils
import com.frame.basic.base.widget.recycler.SectionDecoration
import com.frame.basic.base.widget.recycler.SectionDecorationCallback
import com.frame.basic.common.demo.ui.CommonBaseActivity
import com.frame.module.demo.R
import com.frame.module.demo.bean.ArticleImageBean
import com.frame.module.demo.databinding.DemoItemStyleTextBinding
import com.frame.module.demo.databinding.DemoRecyclerViewBinding
import com.frame.module.demo.databinding.DemoTopSearchBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SingleTypeRecyclerViewActivity :
    CommonBaseActivity<DemoRecyclerViewBinding, SingleTypeRecyclerViewVM>(),
    RecyclerViewBasicPlugin<ArticleImageBean, DemoItemStyleTextBinding> {
    override val mBindingVM: SingleTypeRecyclerViewVM by vms()
    override fun DemoRecyclerViewBinding.initView() {
    }

    override fun DemoRecyclerViewBinding.initListener() {
    }

    override fun bindAdapterListener(adapter: BaseQuickAdapter<ArticleImageBean, BaseDataBindingHolder<DemoItemStyleTextBinding>>) {
        adapter.setOnItemClickListener { _, _, position ->
            getMutableLiveData().value?.get(position)?.apply {
                ToastUtils.showShort(title)
            }
        }
    }

    override fun title() = "RecyclerViewBasicPlugin插件Demo"

    override fun getItemId() = R.layout.demo_item_style_text
    override fun getRecyclerView(): RecyclerView  = mBinding.recyclerView

    override fun convert(
        holder: BaseDataBindingHolder<DemoItemStyleTextBinding>,
        item: ArticleImageBean
    ) {
        holder.dataBinding?.apply {
            titleText.text = item.title
            descText.text = item.desc
        }
    }

    override fun getMutableLiveData(): MutableLiveData<MutableList<ArticleImageBean>> {
        return mBindingVM.articleList
    }

    override fun initRecyclerView(recyclerView: RecyclerView) {
        super.initRecyclerView(recyclerView)
        recyclerView.setBackgroundColor(Color.parseColor("#F3F3F3"))
        recyclerView.addItemDecoration(SectionDecoration(object : SectionDecorationCallback {
            private val mTextSize = 15.dpF
            private val mLeftPadding = 10.dp
            private val mTextPaint = Paint().apply {
                color = Color.RED
                isAntiAlias = true
                textSize = mTextSize
            }
            private val mBackgroundPaint =  Paint().apply {
                color = Color.GREEN
            }
            override fun getSectionTag(position: Int): String {
                val text = mBindingVM.articleList.value?.get(position)?.niceDate
                return if (text.isNullOrEmpty()){
                    ""
                }else{
                    text.substringBefore(" ")
                }
            }

            override fun drawSection(canvas: Canvas, position: Int, left: Int, top: Int, right: Int, bottom: Int) {
                canvas.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), mBackgroundPaint)
                canvas.drawText(getSectionTag(position), left.toFloat() + mLeftPadding, bottom - (getSectionHeight()-mTextSize)/2, mTextPaint)
            }
            override fun getSectionHeight() = 50.dp
        }))
    }

    override fun getTopContainerView(): View {
        val searchBinding = DemoTopSearchBinding.inflate(LayoutInflater.from(this))
        searchBinding.searchEdit.addDelayChangeAfter {
            mBindingVM.searchKey.value = it
        }
        mBindingVM.searchKey.observe(this@SingleTypeRecyclerViewActivity){
            mBindingVM.onRefresh(this)
        }
        return searchBinding.root
    }
}
