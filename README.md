#RippleCompat
----

##Easy To Use!

Init library in your `Activity`:
        
    RippleCompat.init(context);

Add a simple line of code for each view you want to ripple:

    RippleCompat.apply(view);
    
##Demo

![Demo1](/demo/demo1.gif)
![Demo2](/demo/demo2.gif)
![Demo3](/demo/demo3.gif)

##Strength

- Support ripple with view's origin background, or you can set a background image with `scaleType`;
- Support `ImageView`, `TextView`, `Button`, `EditText`. Especially, `android:scaleType` of `ImageView` also works! 
- You can choose these type of ripple: Circle, Heart, Triangle;
- Spinning ripple;
- You can choose ripple full of view or not;
- STILL EXPERIMENTING MORE.

##Dependency

Add dependency in module:

    compile 'com.github.desmond1121:ripplecompat:0.2.0'

##More Customization

    RippleConfig config = new RippleConfig();
    /* set background color */
    config.setBackgroundColor(backgroundColor);
    /* set max ripple radius, invoking this method would override the change by setIsFull */
    config.setMaxRippleRadius(radius);
    /* set fading duration after ripple to max */
    config.setFadeDuration(duration);
    /* set ripple duration */
    config.setRippleDuration(rippleDuration);
    /* set ripple animation interpolator, default is AccelerateInterpolator*/
    config.setInterpolator(interpolator);
    /* set ripple color */
    config.setRippleColor(rippleColor);
    /* set ripple shape type , default is CIRCLE*/
    config.setType(RippleCompatDrawable.Type.HEART);
    /* set background drawable, it would disable the set background color and origin background */
    config.setBackgroundDrawable(drawable);
    /* set scaleType of the set drawable, default is FIT_CENTER */
    config.setScaleType(scaleType);
    /* set spin ripple */
    config.setIsSpin(isSpin);
    /* if ripple full of view, invoking this method would override the change by setMaxRippleRadius */
    config.setIsFull(isFull);
    
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
- Some widget (Button, EditText, etc. ) only perform correctly in **AppCompat style widget**. 

##Dependency

v7 support library.

##LISCENSE
    
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