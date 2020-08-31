package com.example.cloudreaderkotloin.base

import android.app.ActivityManager
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.example.cloudreaderkotloin.bussiness.common.bean.NetWorkDataLoadState
import java.lang.reflect.ParameterizedType
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.javaField


open abstract class BaseActivity<VM : BaseViewModel> : AppCompatActivity() {
    var viewModel: VM? = null

    //layout文件id
    protected abstract val contentViewId: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(contentViewId)

        initViewModel()

    }

    /**
     * 初始化viewmodel
     */
    private fun initViewModel() {
        if (viewModel != null) return;
        var clzz: Class<VM>
        val type = this::class.java.genericSuperclass

        if (type is ParameterizedType) {
            clzz = type.actualTypeArguments[0] as Class<VM>
            viewModel = ViewModelProvider(this).get(clzz)
        } else {
            viewModel = ViewModelProvider(this).get(BaseViewModel::class.java) as VM
        }
    }

    private fun initLoadState() {
        val kClass = viewModel?.javaClass?.kotlin
        kClass?.memberProperties?.forEach {
            if (getGenericClass(it)) {
//                loadState = it.get(viewModel!!) as MutableLiveData<LoadState>
            }
        }
    }





    private fun getGenericClass(property: KProperty1<*, *>): Boolean {

        val type = property.javaField?.genericType
        if (type is ParameterizedType) {
            if (type.actualTypeArguments.size == 1 && type.rawType == MutableLiveData::class.java) {
                val type0 = type.actualTypeArguments[0]
                if (type0 == NetWorkDataLoadState::class.java
                ) {
                    return true
                }
            }
        }
        return false
    }


    /**
     * 判断某个界面是否在前台
     * @return 是否在前台显示
     */
    fun isForeground(): Boolean {
        return isForeground(this, this.javaClass.name)
    }

    /**
     * 判断某个界面是否在前台
     *
     * @param context   Context
     * @param className 界面的类名
     * @return 是否在前台显示
     */
    private fun isForeground(context: Context?, className: String): Boolean {
        if (context == null || TextUtils.isEmpty(className)) return false
        val am =
            context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val list = am.getRunningTasks(1)
        if (list != null && list.size > 0) {
            val cpn = list[0].topActivity
            if (className == cpn!!.className) return true
        }
        return false
    }


}



