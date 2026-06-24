# 记事本 App

## 在线编译（不用装任何开发工具）

### 步骤一：上传代码到 GitHub
1. 在 GitHub 上新建一个仓库（仓库名随意，如 `memo-app`）
2. 把整个 MemoApp 文件夹上传到仓库

### 步骤二：自动编译
上传代码后，GitHub Actions 会自动开始编译（约 2-3 分钟）。

### 步骤三：下载 APK
1. 在 GitHub 仓库页面点击顶部 **Actions** 标签
2. 点击左侧 **Build APK**
3. 找到最新一次运行记录
4. 在页面底部 **Artifacts** 区域，点击 **MemoApp-debug** 下载

下载的 APK 文件传到手机直接安装即可。

---

## 手动触发编译
如果推送代码后没有自动开始编译：
1. 进入 Actions → Build APK
2. 点击 **Run workflow** 按钮手动触发
