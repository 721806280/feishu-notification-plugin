package io.jenkins.plugins.model;

import com.alibaba.fastjson2.JSONArray;
import io.jenkins.plugins.enums.BuildStatusEnum;
import io.jenkins.plugins.sdk.FeiShuTalkRobotRequest;
import io.jenkins.plugins.tools.Utils;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang.StringUtils;

import java.util.Arrays;

/**
 * @author xm.z
 */
@Data
@Builder
public class BuildJobModel {

    private String projectName;

    private String projectUrl;

    private String jobName;

    private String jobUrl;

    private BuildStatusEnum statusType;

    private String duration;

    private String executorName;

    private String executorMobile;

    private String content;

    public String toMarkdown() {
        return Utils.join(
                Arrays.asList(
                        String.format("\uD83D\uDCCB **任务名称**：[%s](%s)", projectName, projectUrl),
                        String.format("\uD83D\uDD22 **任务编号**：[%s](%s)", jobName, jobUrl),
                        String.format("\uD83C\uDF1F **构建状态**:  %s", Utils.dye(statusType.getLabel(), statusType.getColor())),
                        String.format("\uD83D\uDD50 **构建用时**:  %s", duration),
                        String.format("\uD83D\uDC64 **执  行 者**:  %s", executorName),
                        content == null ? "" : content
                )
        );
    }

    public JSONArray toPost() {
        JSONArray of = JSONArray.of(
                JSONArray.of(
                        new FeiShuTalkRobotRequest.Post.Element("text", "\uD83D\uDCCB 任务名称：", ""),
                        new FeiShuTalkRobotRequest.Post.Element("a", projectName, projectUrl)
                ),
                JSONArray.of(
                        new FeiShuTalkRobotRequest.Post.Element("text", "\uD83D\uDD22 任务编号：", ""),
                        new FeiShuTalkRobotRequest.Post.Element("a", jobName, jobUrl)
                ),
                JSONArray.of(new FeiShuTalkRobotRequest.Post.Element("text", String.format("\uD83D\uDCCB 构建状态：%s", statusType.getLabel()), "")),
                JSONArray.of(new FeiShuTalkRobotRequest.Post.Element("text", String.format("\uD83D\uDD50 构建用时：%s", duration), "")),
                JSONArray.of(new FeiShuTalkRobotRequest.Post.Element("text", String.format("\uD83D\uDC64 执  行 者：%s", executorName), ""))
        );

        if (StringUtils.isNotBlank(content)) {
            of.add(JSONArray.parseArray(content));
        }

        return of;
    }
}
