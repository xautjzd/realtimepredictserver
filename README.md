RealTimePredictServer
---

项目简介
---

本项目为毕业设计人体行为识别预测服务端，采用Servlet实现，预测模型之前已经训练好，主要包括SVM和DecisionTree。Android客户端实时收集手机内置的三轴加速度数据，并暂存。当暂存数据满足指定窗口大小(20)时，便进行特征提取，提取的特征主要包括：

- 均值
- 方差
- 两轴之间相关系数
- 偏度
- 峰度

提取过程完成后，通过Servlet请求将之前提取的特征封装成对象传递到预测服务端，服务端进行解析，并调用预测模型进行动作的识别，目前识别的动作主要包含以下几个：

- 上楼
- 下楼
- 步行
- 站立

代码类简要介绍
---

1. PredictAttribute
存储Android客户端传过来的封装好的待识别特征