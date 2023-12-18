## <div align="center">ColorPicker</div>
#### <div align="center">A color picker library for Android.</div>

## Download

[![](https://jitpack.io/v/Atharok/ColorPicker.svg)](https://jitpack.io/#Atharok/ColorPicker)

Add it in your root build.gradle at the end of repositories:

```groovy
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

Add the dependency in your app build.gradle:

```groovy
dependencies {
	...
	implementation 'com.github.Atharok:ColorPicker:1.0.1'
}
```

## Usage

An example of use is available here:
[/app/src/main/java/com/atharok/colorpickerapp/MainActivity.kt](https://github.com/Atharok/ColorPicker/blob/main/app/src/main/java/com/atharok/colorpickerapp/MainActivity.kt)
[/app/src/main/res/layout/activity_main.xml](https://github.com/Atharok/ColorPicker/blob/main/app/src/main/res/layout/activity_main.xml)

### Sample

In your XML layout:

```xml
<com.atharok.colorpicker.ColorPicker
	android:id="@+id/color_picker"
	android:layout_width="match_parent"
	android:layout_height="wrap_content"
	android:padding="8dp" />
```

In your Kotlin Activity:

```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
	super.onCreate(savedInstanceState)
	setContentView(R.layout.activity_main)

	val colorPicker: ColorPicker = findViewById(R.id.color_picker)

	// Called when color changes
	colorPicker.addColorChangedListener { color: Int, hexColorARGB: String ->
		...
	}
}
```

### Dialog

You can also use the color picker in a dialog box:

```kotlin
val builder = ColorPickerDialog.Builder(this)
val colorPickerDialog = builder.apply {
	setTitle(R.string.dialog_title)
	setDefaultColor(Color.RED)
	setPositiveButton { color: Int, hexColorARGB: String ->
		...
	}
}.show()
```

# License

```txt
Copyright 2023 Atharok

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

	http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

