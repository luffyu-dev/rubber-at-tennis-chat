package com.rubber.at.tennis.chat.api.enums;

import lombok.Getter;

/**
 * @author luffyu
 * Created on 2024/1/21
 *      * queued：Run首次创建或完成所需操作后，将其移至排队状态。它们几乎立即转到in_progress状态。
 *      * in_progress：助手使用模型和工具执行步骤。您可以通过检查Run Steps来查看Run的进度。
 *      * completed：运行成功完成！您现在可以查看Assistant添加到Thread中的所有Message以及运行所采取的所有步骤。您还可以通过向Thread添加更多用户消息并创建另一个Run来继续对话。
 *      * requires_action：当使用function-calling工具时，一旦模型确定要调用的函数的名称和参数，运行将转移到requires_action状态。
 *      * expired：当函数调用输出在expires_at之前未提交并且运行过期时，会发生这种情况。此外，如果运行时间太长，超过expires_at中所述的时间，我们的系统将使运行过期。
 *      * cancelling：您可以尝试使用https://platform.openai.com/docs/api-reference/runs/cancelRun端点取消正在进行中的运行
 *      * cancelled：运行已成功取消。
 *      * failed：您可以通过查看Run中的last_error对象来查看失败的原因。失败的时间戳将记录在failed_at下。
 *      *
 */
@Getter
public enum MsgStatusEnums {

    QUEUED("queued",""),
    IN_PROGRESS("in_progress",""),
    COMPLETED("completed",""),
    REQUIRES_ACTION("requires_action",""),
    EXPIRED("expired",""),
    CANCELLING("cancelling",""),
    CANCELLED("cancelled",""),
    FAILED("failed",""),

    ;


    MsgStatusEnums(String status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    private final String status;

    private final String desc;

}
