#RippleCompat
----

- Support ripple with background set;
- You can choose these type of ripple: Circle, Heart, Triangle;
- Spinning ripple;
- You can choose ripple full of view or not;
- STILL EXPERIMENTING.

##Demo

![Demo1](/demo/demo1.gif)
![Demo2](/demo/demo2.gif)

##How to use?

Include `jcenter` in your project:

    repositories {
        jcenter()
    }
    
add dependency in module:

    compile 'com.desmond.ripple:ripplecompat:0.1.0'

Init library in your `Activity`:
        
    RippleCompat.init(context);

Use below code for each view you want to ripple:

    RippleCompat.apply(view);

or more customization:

    RippleConfig config = new RippleConfig();
    config.setBackgroundColor(backgroundColor);
    config.setIsFull(isFull);
    config.setFadeDuration(duration);
    config.setRippleDuration(rippleDuration);
    config.setInterpolator(interpolator);
    config.setRippleColor(rippleColor);
    config.setType(RippleCompatDrawable.Type.HEART);
    config.setIsSpin(isSpin);
    config.setIsFull(isFull);
    
    RippleCompat.apply(view, config, new RippleCompatDrawable.OnFinishListener() {
                                                 @Override
                                                 public void onFinish() {
                                                     Snackbar.make(v, "Ripple Finish!", Snackbar.LENGTH_SHORT).show();
                                                 }
                                             });

##Known Drawbacks

- `ScaleType` of `ImageView` would be reset to `fitXY`;
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