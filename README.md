# Kotlinfilepicker [![](https://jitpack.io/v/androidbuffer/kotlinfilepicker.svg)](https://jitpack.io/#androidbuffer/kotlinfilepicker)

An Android file picker written all in [Kotlin](https://kotlinlang.org/docs/reference/). KotlinFilePicker can be used to pick media files from Gallery and storage device. 

## How to Download:
Download the latest <b>.aar</b> and sample <b>.apk</b> from [release](https://github.com/androidbuffer/Kotlinfilepicker/releases). 

<b>Gradle</b>

Add it in your root build.gradle at the end of repositories:
```
allprojects {
    repositories {
        ...
        maven {
            url 'https://jitpack.io'
        }
    }
}
```
Add the dependency
```
dependencies {
    implementation 'com.github.androidbuffer:kotlinfilepicker:v0.0.1-alpha'
}
```
<b>Maven</b>
```
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```
Add the dependency
```
<dependency>
    <groupId>com.github.androidbuffer</groupId>
    <artifactId>kotlinfilepicker</artifactId>
    <version>v0.0.1-alpha</version>
</dependency>
```
## Usage
* make a camera request
```
KotRequest.Camera(this, REQUEST_CAMERA).pick()
//or
KotRequest.Camera(this).setRequestCode(REQUEST_CAMERA).pick()
//or get a intent back
var intent = KotRequest.Camera(this, REQUEST_CAMERA).getCameraIntent()
```
* make a video request
```
KotRequest.Video(this, REQUEST_VIDEO).pick()
```
* make a gallery request
```
KotRequest.Gallery(this, REQUEST_GALLERY).isMultiple(isMultiple).pick()
```
* make a file pick request
```
KotRequest.File(this, REQUEST_FILE)
                .isMultiple(isMultiple)
                .setMimeType(KotConstants.FILE_TYPE_FILE_ALL)
                .pick()
```
## Contributing / Issues
we would be thankful for contributing to this project or if you find some bug or suggestions we welcome you also.



## Authors

* **Vikas kumar**

See also the list of [contributors](https://github.com/androidbuffer/Kotlinfilepicker/graphs/contributors) who participated in this project.

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE) file for details
