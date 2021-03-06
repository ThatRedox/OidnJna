## Java JNA interface for Intel Open Image Denoise (OIDN)

This is a library that provides a Java interface to [Intel Open Image Denoise](https://www.openimagedenoise.org/)
through [JNA](https://github.com/java-native-access/jna).

## Before
![image](https://user-images.githubusercontent.com/42661490/148583713-b889ee39-1ad4-481c-b449-5e6168285ec5.png)
Rendered at 128 spp using [Chunky](https://chunky-dev.github.io/docs/).

## Denoised
![image](https://user-images.githubusercontent.com/42661490/148583755-1efa8d87-282f-400a-9e46-58e81cccb869.png)
Denoised with 64 spp albedo and normal maps.

## How to use
```java
OpenImageDenoise oidn = new OpenImageDenoise("<path to denoiser library>");
float[] input = <your image here>;
int width = <image height>;
int height = <image width>;
float[] output;

// Create a device. Use try with resources to ensure it is cleaned up
// when we are done with it. Note that all OIDN objects implement AutoCloseable
// but will still attempt to clean themselves up once there are no references
// to them.
try (Device device = oidn.createDevice()) {
    // Commit device settings.
    device.commit();
    
    // Create OIDN buffers.
    Buffer outputBuffer = device.createBuffer(input.length);
    Buffer inputBuffer = device.createBuffer(input.length);
    inputBuffer.writeBuffer(input);
    
    // Create a RT filter.
    try (Filter filter = device.createFilter("RT")) {
        // See https://www.openimagedenoise.org/documentation.html for a
        // complete list of parameters
        filter.setFilterParam("hdr", true);
        
        filter.setFilterImage("output", outputBuffer, width, height);
        filter.setFilterImage("color", inputBuffer, width, height);
        
        filter.commit();
        
        // Do the denoising
        filter.execute();
        
        // Read out the output into Java again
        output = outputBuffer.readBuffer();
    }
    
    // Clean up the buffers
    outputBuffer.close();
    inputBuffer.close();
}
```
