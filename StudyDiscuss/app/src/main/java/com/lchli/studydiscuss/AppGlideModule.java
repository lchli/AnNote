package com.lchli.studydiscuss;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemoryCache;
import com.bumptech.glide.module.GlideModule;

public class AppGlideModule implements GlideModule {

    private static final int GLIDE_MEM_CACHE = 5 * 1024 * 1024;

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        // Apply options to the builder here.
        MemoryCache gildeMemoryCache = new LruResourceCache(GLIDE_MEM_CACHE);
        builder.setMemoryCache(gildeMemoryCache);
    }

    @Override
    public void registerComponents(Context context, Glide glide) {
        // register ModelLoaders here.
    }
}