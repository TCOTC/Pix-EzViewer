/* * MIT License * * Copyright (c) 2020 ultranity * Copyright (c) 2019 Perol_Notsfsssf * * Permission is hereby granted, free of charge, to any person obtaining a copy * of this software and associated documentation files (the "Software"), to deal * in the Software without restriction, including without limitation the rights * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell * copies of the Software, and to permit persons to whom the Software is * furnished to do so, subject to the following conditions: * * The above copyright notice and this permission notice shall be included in all * copies or substantial portions of the Software. * * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE * SOFTWARE */package com.perol.asdpl.pixivez.activityimport android.content.Contextimport android.content.Intentimport android.graphics.Colorimport android.os.Bundleimport android.view.Menuimport android.view.MenuItemimport android.view.Viewimport android.view.WindowManagerimport androidx.preference.PreferenceManagerimport androidx.viewpager.widget.ViewPagerimport com.perol.asdpl.pixivez.Rimport com.perol.asdpl.pixivez.adapters.PicturePagerAdapterimport com.perol.asdpl.pixivez.objects.DataHolderimport com.perol.asdpl.pixivez.objects.ThemeUtilimport com.perol.asdpl.pixivez.responses.Illustimport kotlinx.android.synthetic.main.activity_picture.*import java.util.ArrayListclass PictureActivity : RinkActivity() {    companion object {        fun startSingle(context: Context, id: Long) {            val bundle = Bundle()            val arrayList = LongArray(1)            arrayList[0] = id            bundle.putLongArray("illustidlist", arrayList)            bundle.putLong("illustid", id)            val intent = Intent(context, PictureActivity::class.java)            intent.putExtras(bundle)            context.startActivity(intent)        }        fun startSingle(context: Context, illust: Illust) {            val bundle = Bundle()            val arrayList = LongArray(1)            arrayList[0] = illust.id            bundle.putLongArray("illustidlist", arrayList)            bundle.putParcelable("illust",illust)            val intent = Intent(context, PictureActivity::class.java)            intent.putExtras(bundle)            context.startActivity(intent)        }    }    private var illustId: Long = 0    private var illustIdList: LongArray? = null    private var illustList: ArrayList<Illust>? = null    private var nowPosition: Int = 0    override fun onCreate(savedInstanceState: Bundle?) {        super.onCreate(savedInstanceState)        ThemeUtil.themeInit(this)        setContentView(R.layout.activity_picture)        supportPostponeEnterTransition()        if (!PreferenceManager.getDefaultSharedPreferences(this).getBoolean(                "needstatusbar",                false            )        ) {            val window = window            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)            window.decorView.systemUiVisibility =                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)            window.statusBarColor = Color.TRANSPARENT        }        val bundle = this.intent.extras        illustId = bundle?.getLong("illustid")!!        if (bundle.getLongArray("illustidlist") != null) {            illustIdList = bundle.getLongArray("illustidlist")            nowPosition = illustIdList!!.indexOf(illustId)            viewpage_picture!!.adapter = PicturePagerAdapter(supportFragmentManager, illustIdList!!)        }        else {            illustList = DataHolder.getIllustsList()            illustIdList = if (illustList != null) illustList!!.map { it.id }.toLongArray()                                            else  LongArray(1) { illustId }            nowPosition = bundle.getInt("position",0)            viewpage_picture!!.adapter =  PicturePagerAdapter(supportFragmentManager, illustIdList, illustList)            DataHolder.pictureAdapter = viewpage_picture!!.adapter            }        viewpage_picture.currentItem = nowPosition        setSupportActionBar(toolbar)        supportActionBar!!.setDisplayHomeAsUpEnabled(true)        supportActionBar!!.setDisplayShowTitleEnabled(false)        viewpage_picture.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {            override fun onPageScrollStateChanged(state: Int) {            }            override fun onPageScrolled(                position: Int,                positionOffset: Float,                positionOffsetPixels: Int            ) {            }            override fun onPageSelected(position: Int) {                nowPosition = position            }        })    }    override fun onCreateOptionsMenu(menu: Menu): Boolean {        menuInflater.inflate(R.menu.menu_picture, menu)        return super.onCreateOptionsMenu(menu)    }    override fun onOptionsItemSelected(item: MenuItem): Boolean {        when (item.itemId) {            android.R.id.home ->                finishAfterTransition()            R.id.action_share -> share()        }        return super.onOptionsItemSelected(item)    }    override fun onDestroy() {        DataHolder.pictureAdapter = null        super.onDestroy()    }    private fun share() {        val textIntent = Intent(Intent.ACTION_SEND)        //val illustId = illustIdList?.get(nowPosition)?: illustList?.get(nowPosition)?.id        textIntent.type = "text/plain"        textIntent.putExtra(            Intent.EXTRA_TEXT,            "https://www.pixiv.net/member_illust.php?illust_id=${illustIdList!![nowPosition]}&mode=medium"        )        startActivity(Intent.createChooser(textIntent, getString(R.string.share)))    }}