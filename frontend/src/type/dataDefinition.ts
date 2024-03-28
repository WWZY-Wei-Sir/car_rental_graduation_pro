export enum UserStatus {
    Normal = "正常" ,
    Block = "封禁",
    Wrong = "Wrong",
}

export enum UserType {
    Admin = "管理员",
    Worker = "员工",
    Customer = "顾客",
    Wrong = "Wrong",
}

export enum CarStatus {
    Normal = "正常",
    Maintenance = "维修中",
    Annual_Inspection = "年检中",
    Wrong = "Wrong",
}

export enum OrderStatus {
    WAIT_DEPOSIT = "待下定金",
    WAIT_AUDIT = "待审核",
    WAIT_TAKE_CAR = "待拿车",
    WAIT_CHECK_FILE = "待检查文件",
    WAIT_RETURN_CAR = "待还车",
    WAIT_WORKER_CAL = "待工作人员检查",
    WAIT_REMAINING = "待付剩余金额",
    CANCEL = "取消",
    FINISH = "结束",
    Wrong = "Wrong",
}
