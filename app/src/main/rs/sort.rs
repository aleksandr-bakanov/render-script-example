#pragma version(1)
#pragma rs java_package_name(com.example.bav.renderscriptexample)

rs_script gScript;
// Array allocation
rs_allocation arr;
rs_allocation dummy;

// Size of array
int size;
// Currently processed elements: 0 - evens, 1 - odds
static int parity;
// Flag showing at least one swap has occured: 0 - false, 1 - true
static int swap_occurred;

static void initialize() {
    parity = 0;
    swap_occurred = 0;
}

static void swap_parity() {
    if (parity == 0) parity = 1;
    else parity = 0;
    rsDebug("sort.rs: swap_parity {p, s} ", parity, swap_occurred);
}

static void reset_swap_flag() {
    rsDebug("sort.rs: reset_swap_flag", 0);
    swap_occurred = 0;
}

static void compare_and_swap(int in, uint32_t x) {
    int neighbor = rsGetElementAt_int(arr, x + 1);
    if (neighbor < in) {
        rsDebug("sort.rs: compare_and_swap x, in = ", x, in);
        rsSetElementAt_int(arr, neighbor, x);
        rsSetElementAt_int(arr, in, x + 1);
        swap_occurred = 1;
    }
}

int RS_KERNEL execute(int in, uint32_t x) {
    rsDebug("sort.rs: execute {x, in} =", x, in);
    // Last element will never be processed by itself
    if (x < size - 1) {
        // Swaping even elements
        if (parity == 0) {
            if ((x & 1) == 0) {
                compare_and_swap(in, x);
            }
        }
        // Swaping odd elements
        else if ((x & 1) == 1) {
            compare_and_swap(in, x);
        }
    }
    // Result is dummy
    return 0;
}

void start() {
    initialize();
    do {
        reset_swap_flag();
        rsForEach(execute, arr, dummy);
        swap_parity();
        rsForEach(execute, arr, dummy);
    } while (swap_occurred == 1);
}
