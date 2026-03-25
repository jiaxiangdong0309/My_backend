#!/bin/bash

# 花生溯源系统 - 一键重启脚本
# 用法: ./restart.sh [模块名]
# 示例: ./restart.sh        # 编译全部，启动 gateway
#       ./restart.sh auth   # 只重启 auth 模块

# 颜色输出
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${YELLOW}========== 花生溯源系统 - 一键重启 ==========${NC}"

# 获取脚本所在目录（项目根目录）
PROJECT_DIR="$(cd "$(dirname "$0")" && pwd)"
cd "$PROJECT_DIR"

# 参数处理
TARGET_MODULE=${1:-gateway}

echo -e "${GREEN}[1/3] 正在编译整个项目...${NC}"
mvn clean install -DskipTests -q

if [ $? -ne 0 ]; then
    echo -e "${RED}编译失败！请检查错误信息${NC}"
    exit 1
fi

echo -e "${GREEN}[2/3] 编译成功！${NC}"

# 检查模块是否存在
MODULE_DIR="peanut-${TARGET_MODULE}"
if [ ! -d "$MODULE_DIR" ]; then
    echo -e "${RED}错误: 模块 $TARGET_MODULE 不存在${NC}"
    echo "可用模块: common, auth, product, trace, stats, gateway"
    exit 1
fi

echo -e "${GREEN}[3/3] 正在启动 $TARGET_MODULE 模块...${NC}"
echo -e "${YELLOW}=============================================${NC}"

# 进入模块目录并启动
cd "$MODULE_DIR"
mvn spring-boot:run
