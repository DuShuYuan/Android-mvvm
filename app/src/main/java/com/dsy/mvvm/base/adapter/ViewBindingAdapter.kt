package com.dsy.mvvm.base.adapter

import android.util.SparseArray
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.dsy.mvvm.utils.inflateBindingWithGeneric

class VBBaseViewHolder<VB : ViewBinding>(val binding: VB) : BaseViewHolder(binding.root)
/**
 * Adapter基类
 * 使用：
 *
class MyAdapter:VBBaseQuickAdapter<String,ABinding>(){
    override fun convert(holder: VBBaseViewHolder<ABinding>, item: String) {
        holder.binding...
    }
}
*/
abstract class VBQuickAdapter<T, VB : ViewBinding> :BaseQuickAdapter<T, VBBaseViewHolder<VB>>(0) {

    override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): VBBaseViewHolder<VB> {
        val viewBinding = inflateBindingWithGeneric<VB>(parent)
        return VBBaseViewHolder(viewBinding)
    }
}
/**
 * 多布局Adapter基类
 * 使用：
 *
class MyAdapter:VBBaseMultiItemQuickAdapter<MultiItemEntity>(){
    init {
        addViewBinding(0, ABinding::inflate)
        addViewBinding(1, BBinding::inflate)
    }
    override fun convert(holder: VBBaseViewHolder<ViewBinding>, item: MultiItemEntity) {
        holder.binding.apply {
            if (this is ABinding) {
                ...
            }else if (this is BBinding){
                ...
            }
        }
    }
}
 */
abstract class VBMultiItemQuickAdapter <T : MultiItemEntity>: BaseMultiItemQuickAdapter<T, VBBaseViewHolder<ViewBinding>>() {

    private val bindings: SparseArray<(LayoutInflater, ViewGroup, Boolean) -> ViewBinding> by lazy(LazyThreadSafetyMode.NONE) {
        SparseArray<(LayoutInflater, ViewGroup, Boolean) -> ViewBinding>()
    }

    fun addViewBinding(type: Int,inflate: (LayoutInflater, ViewGroup, Boolean) -> ViewBinding){
        bindings.put(type, inflate)
    }

    override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): VBBaseViewHolder<ViewBinding> {
        val inflate = bindings.get(viewType)
        require(inflate != null) { "ViewType: $viewType found ViewBinding，please use addViewBinding() first!" }
        val viewBinding = inflate(LayoutInflater.from(parent.context), parent,false)
        return VBBaseViewHolder(viewBinding)
    }

}