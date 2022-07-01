# simprokcore-android

## Introduction

This library is the extension of [simprokcore-kotlin](https://github.com/simprok-dev/simprokcore-kotlin). It provides a utility class to connect android's ```Activity``` to [Core](https://github.com/simprok-dev/simprokcore-kotlin/wiki/Core) object via ```ViewModel```. 

If you have come here from [simprokmachine-android](https://github.com/simprok-dev/simprokmachine-android) then you will see that most of the API is the same because of the common [simprokandroid](https://github.com/simprok-dev/simprokandroid) dependency.

## Usage

### Activity

After installation you get two extension methods in your activity.

```Kotlin
fun <State, Event> AppCompatActivity.start(
    assemble: Mapper<WidgetMachine<State, Event>, Assembly>
)
```

and 

```Kotlin
fun <State, Event> AppCompatActivity.startWithRenderer(
    renderer: BiHandler<State?, Handler<Event>>,
    assemble: Mapper<WidgetMachine<State, Event>, Assembly>
)
```

The [start()](https://github.com/simprok-dev/simprokandroid/wiki/AppCompatActivityExt#start-without-renderer) method builds your applicaton's machine hierarchy while [startWithRenderer()](https://github.com/simprok-dev/simprokandroid/wiki/AppCompatActivityExt#start-with-renderer) also adds your ```Activity``` as a [ChildMachine](https://github.com/simprok-dev/simprokmachine-kotlin/wiki/ChildMachine) in this hierarchy enabling you to render UI according to the coming input.

This is done via special [Assembly](https://github.com/simprok-dev/simprokandroid/wiki/Assembly) object and its inhertitor - ```CoreAssembly```.

```Kotlin
start<ActivityInput, ActivityOutput> { activityMachine -> 
    CoreAssembly.create(
        WidgetLayer.Object(activityMachine, stateMapper, eventMapper),
        secondary = setOf() // .. other layers
    ) { state, event -> 
        // ReducerResult should be returned here
    }
}
```

### Fragment

Inside your fragments you can call [filterMapInput()](https://github.com/simprok-dev/simprokandroid/wiki/FragmentExt#filtermapinput), [filterMapOutput()](https://github.com/simprok-dev/simprokandroid/wiki/FragmentExt#filtermapoutput) or [render()](https://github.com/simprok-dev/simprokandroid/wiki/FragmentExt#render) to filter input flow, filter output flow or subscribe to the flow to render fragment's UI.

```Kotlin
fun <ParentInput, ParentOutput, ChildOutput> Fragment.filterMapOutput(
    func: Mapper<ChildOutput, FilterMap<ParentOutput>>
): RenderingObject<ParentInput, ChildOutput>
```

```Kotlin
fun <ParentInput, ParentOutput, ChildInput> Fragment.filterMapInput(
    func: Mapper<ParentInput?, FilterMap<ChildInput?>>
): RenderingObject<ChildInput, ParentOutput>
```

```Kotlin
fun <Input, Output> Fragment.render(func: BiHandler<Input?, Handler<Output>>)
```


[RenderingObject](https://github.com/simprok-dev/simprokandroid/wiki/RenderingObject) is class with builder pattern inside if more filtering operators application needed.

## Example

Check out the [sample](https://github.com/simprok-dev/simprokcore-android/tree/main/sample) for more information about API and how to use it.


## Installation

Add this in your project's gradle file:

```groovy
implementation 'com.github.simprok-dev:simprokcore-android:1.1.9'
```

and this in your settings.gradle file:

```groovy
repositories {
    ...
    maven { url 'https://jitpack.io' }
}
```
