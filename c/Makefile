CC=gcc
CFLAGS=-Wall -Wpedantic
LIBFLAGS=# Add your library specific flags here
DEBUG_CFLAGS=-g
CFLAGS+=$(LIBFLAGS)

MKDIR = mkdir -p $(OUT_DIR) $(dir $@)
RM = rm -rf

SRC_DIR=src
OUT_DIR=bin
TARGET=main

SRC_FILES := $(wildcard $(SRC_DIR)/**/*.c) $(wildcard $(SRC_DIR)/*.c)
OBJ_FILES := $(patsubst $(SRC_DIR)/%.c,$(OUT_DIR)/%.o,$(SRC_FILES))
DEP_FILES := $(OBJ_FILES:.o=.d)

$(OUT_DIR)/%.o: $(SRC_DIR)/%.c
	$(MKDIR)
	$(CC) -c -o $@ $< $(CFLAGS)

# Include auto-generated dependency files
-include $(DEP_FILES)

all: $(TARGET)

# Target rules
debug: CFLAGS += $(DEBUG_CFLAGS)
debug: $(TARGET)

$(TARGET): $(OBJ_FILES)
	$(CC) -o $@ $^ $(CFLAGS)

.PHONY: clean

clean:
	$(RM) $(OUT_DIR) $(TARGET)$(BIN_EXT)