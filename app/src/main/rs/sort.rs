#pragma version(1)
#pragma rs java_package_name(com.example.bav.renderscriptexample)

// Array
int *array;
// Size of array
int size;
// Currently processed elements: 0 - evens, 1 - odds
int parity;
// Flag showing at least one swap has occured: 0 - false, 1 - true
int swapOccurred;

void initialize() {
    parity = 0;
    swapOccurred = 0;
}

void next_pass() {
    if (parity == 0) parity = 1;
    else parity = 0;
    swapOccurred = 0;
}

int RS_KERNEL execute(int in, uint32_t x) {
    return 0;
}
