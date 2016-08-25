/**
 * Copyright (C) 2010-2014 eBusiness Information, Excilys Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed To in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.banditcat.android.transformer.compiler.helper;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class CanonicalNameConstants {

	/*
	 * Java
	 */
	public static final String OBJECT = Object.class.getCanonicalName();
	public static final String URI = java.net.URI.class.getCanonicalName();
	public static final String MAP = Map.class.getCanonicalName();
	public static final String SET = Set.class.getCanonicalName();
	public static final String LIST = List.class.getCanonicalName();
	public static final String COLLECTION = Collection.class.getCanonicalName();
	public static final String COLLECTIONS = Collections.class.getCanonicalName();
	public static final String STRING = String.class.getCanonicalName();
	public static final String STRING_BUILDER = StringBuilder.class.getCanonicalName();
	public static final String STRING_SET = "java.util.Set<java.lang.String>";
	public static final String CHAR_SEQUENCE = CharSequence.class.getCanonicalName();
	public static final String SQL_EXCEPTION = SQLException.class.getCanonicalName();
	public static final String INTEGER = Integer.class.getCanonicalName();
	public static final String BOOLEAN = Boolean.class.getCanonicalName();
	public static final String ARRAYLIST = ArrayList.class.getCanonicalName();
	public static final String SERIALIZABLE = Serializable.class.getCanonicalName();

	/*
	 * Android
	 */
	public static final String LOG = "android.util.Log";
	public static final String PARCELABLE = "android.os.Parcelable";
	public static final String INTENT = "android.content.Intent";
	public static final String INTENT_FILTER = "android.content.IntentFilter";
	public static final String COMPONENT_NAME = "android.content.ComponentName";
	public static final String BUNDLE = "android.os.Bundle";
	public static final String APPLICATION = "android.app.Application";
	public static final String ACTIVITY = "android.app.Activity";
	public static final String EDITABLE = "android.text.Editable";
	public static final String TEXT_WATCHER = "android.text.TextWatcher";
	public static final String SEEKBAR = "android.widget.SeekBar";
	public static final String ON_SEEKBAR_CHANGE_LISTENER = "android.widget.SeekBar.OnSeekBarChangeListener";
	public static final String TEXT_VIEW = "android.widget.TextView";
	public static final String TEXT_VIEW_ON_EDITOR_ACTION_LISTENER = "android.widget.TextView.OnEditorActionListener";
	public static final String COMPOUND_BUTTON = "android.widget.CompoundButton";
	public static final String COMPOUND_BUTTON_ON_CHECKED_CHANGE_LISTENER = "android.widget.CompoundButton.OnCheckedChangeListener";
	public static final String VIEW = "android.view.View";
	public static final String VIEW_ON_CLICK_LISTENER = "android.view.View.OnClickListener";
	public static final String VIEW_ON_TOUCH_LISTENER = "android.view.View.OnTouchListener";
	public static final String VIEW_ON_LONG_CLICK_LISTENER = "android.view.View.OnLongClickListener";
	public static final String VIEW_ON_FOCUS_CHANGE_LISTENER = "android.view.View.OnFocusChangeListener";
	public static final String VIEW_GROUP_LAYOUT_PARAMS = "android.view.ViewGroup.LayoutParams";
	public static final String VIEW_GROUP = "android.view.ViewGroup";
	public static final String CONTEXT = "android.content.Context";
	public static final String KEY_EVENT = "android.view.KeyEvent";
	public static final String LAYOUT_INFLATER = "android.view.LayoutInflater";
	public static final String FRAGMENT_ACTIVITY = "android.support.v4.app.FragmentActivity";
	public static final String FRAGMENT = "android.app.Fragment";
	public static final String SUPPORT_V4_FRAGMENT = "android.support.v4.app.Fragment";
	public static final String HTML = "android.text.Html";
	public static final String WINDOW_MANAGER_LAYOUT_PARAMS = "android.view.WindowManager.LayoutParams";
	public static final String ADAPTER_VIEW = "android.widget.AdapterView";
	public static final String ON_ITEM_CLICK_LISTENER = "android.widget.AdapterView.OnItemClickListener";
	public static final String ON_ITEM_LONG_CLICK_LISTENER = "android.widget.AdapterView.OnItemLongClickListener";
	public static final String ON_ITEM_SELECTED_LISTENER = "android.widget.AdapterView.OnItemSelectedListener";
	public static final String WINDOW = "android.view.Window";
	public static final String MENU_ITEM = "android.view.MenuItem";
	public static final String MENU_INFLATER = "android.view.MenuInflater";
	public static final String MENU = "android.view.Menu";
	public static final String ANIMATION = "android.view.animation.Animation";
	public static final String ANIMATION_UTILS = "android.view.animation.AnimationUtils";
	public static final String RESOURCES = "android.content.res.Resources";
	public static final String CONFIGURATION = "android.content.res.Configuration";
	public static final String MOTION_EVENT = "android.view.MotionEvent";
	public static final String HANDLER = "android.os.Handler";
	public static final String SERVICE = "android.app.Service";
	public static final String INTENT_SERVICE = "android.app.IntentService";
	public static final String BROADCAST_RECEIVER = "android.content.BroadcastReceiver";
	public static final String LOCAL_BROADCAST_MANAGER = "android.support.v4.content.LocalBroadcastManager";
	public static final String CONTENT_PROVIDER = "android.content.ContentProvider";
	public static final String SQLITE_DATABASE = "android.database.sqlite.SQLiteDatabase";
	public static final String KEY_STORE = "java.security.KeyStore";
	public static final String SQLLITE_OPEN_HELPER = "android.database.sqlite.SQLiteOpenHelper";
	public static final String VIEW_SERVER = "org.androidannotations.api.ViewServer";
	public static final String LOOPER = "android.os.Looper";
	public static final String POWER_MANAGER = "android.os.PowerManager";
	public static final String WAKE_LOCK = "android.os.PowerManager.WakeLock";

	/*
	 * Android permission
	 */
	public static final String INTERNET_PERMISSION = "android.permission.INTERNET";
	public static final String WAKELOCK_PERMISSION = "android.permission.WAKE_LOCK";


	private CanonicalNameConstants() {
	}

}
