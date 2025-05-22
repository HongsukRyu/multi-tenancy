package com.multitenancy.back.common.model;

import com.douzone.back.common.object.enumData.ApplicantTotalStatus;
import com.douzone.back.common.object.enumData.CompanyDecisionStatus;
import com.douzone.back.common.object.enumData.InterviewTotalStatus;
import com.douzone.back.common.object.enumData.ReactionType;

public class GlobalConst {

    /**
     * System History type
     */
    // Action type
    public static final String SYSTEM_SEARCH = "search";
    public static final String SYSTEM_CREATE = "create";
    public static final String SYSTEM_UPDATE = "update";
    public static final String SYSTEM_DELETE = "delete";
    public static final String SYSTEM_EXCEL = "excel";

    public static final String DECISION_STATUS_UNCHECKED = ApplicantTotalStatus.unchecked.name();
    public static final String DECISION_STATUS_SUPER_PASS = CompanyDecisionStatus.superpass.name();
    public static final String DECISION_STATUS_PASS = ApplicantTotalStatus.pass.name();
    public static final String DECISION_STATUS_FAILED = ApplicantTotalStatus.failed.name();
    public static final String DECISION_STATUS_PENDING = ApplicantTotalStatus.pending.name();
    public static final String DECISION_STATUS_CANCEL = InterviewTotalStatus.cancel.name();


    public static final String REACTION_STATUS_POSITIVE = ReactionType.positive.name();
    public static final String REACTION_STATUS_NEGATIVE = ReactionType.negative.name();
}
