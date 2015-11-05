#RippleCompat

##Easy To Use!

Init library in your `Activity`:
        
    RippleCompat.init(context);

Add a simple line of code for view or widget you want to ripple:

    RippleCompat.apply(something, rippleColor);
    
##Demo

Demo of simple use and background with scaleType:

![Demo](/demo/Demo.gif)

Demo of palette:

![Palette](/demo/Demo1.gif)

##Strength

- Support ripple with view's origin background, or you can set a background image with `scaleType`!
- Auto PALETTE ripple color with the background image!
- Support `ImageView`, `TextView`, `Button`, `EditText`. Especially, `android:scaleType` of `ImageView` also works! 
- You can choose these type of ripple: Circle, Heart, Triangle;
- Spinning ripple;
- STILL EXPERIMENTING MORE.

##Dependency

Add dependency in module:

    compile 'com.github.desmond1121:ripplecompat:1.2.1'

##More Customization

    RippleConfig config = new RippleConfig();
    config.setMaxRippleRadius(radius); /* set max ripple radius, invoking this method would override the change by setIsFull */
    config.setFadeDuration(duration); /* set fading duration after ripple to max */
    config.setRippleDuration(rippleDuration); /* set ripple duration */
    config.setInterpolator(interpolator); /* set ripple animation interpolator, default is AccelerateInterpolator*/
    config.setRippleColor(rippleColor); /* set ripple color */
    config.setType(RippleCompatDrawable.Type.HEART); /* set ripple shape type , default is CIRCLE*/
    config.setBackgroundDrawable(drawable); /* set background drawable, it would disable the origin background */
    config.setScaleType(ImageView.ScaleType.FIT_CENTER); /* set scaleType of the set drawable, default is FIT_CENTER */
    config.setIsEnablePalette(true); /* enable palette */
    config.setPaletteMode(RippleUtil.PaletteMode.VIBRANT); /* set palette mode (1-16) */
    config.setIsSpin(isSpin); /* set spin ripple */
    config.setIsFull(isFull); /* if ripple full of view, invoking this method would override the change by setMaxRippleRadius */
    
    /* Apply config and add listener */
    RippleCompat.apply(view, config, new RippleCompatDrawable.OnFinishListener() {
        @Override
        public void onFinish() {
         Snackbar.make(v, "Ripple Finish!", Snackbar.LENGTH_SHORT).show();
        }
    });

##Drawback and Tips

- Applying in `ImageView` or setting background would disable the ripple background color.
- It would disable the `OnTouchListener` of the set view. (click listener would be triggered as normal)

##Dependency

- `com.android.support:appcompat-v7:22.2.1`
- `com.android.support:palette-v7:22.2.1`

##LICENSE
    
    Copyright 2015 Desmond Yao
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
     http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.