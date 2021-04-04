package com.eris.gitlabanalyzer.service;

import com.eris.gitlabanalyzer.repository.IssueCommentRepository;
import com.eris.gitlabanalyzer.repository.MergeRequestCommentRepository;
import com.eris.gitlabanalyzer.viewmodel.NoteView;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@lombok.RequiredArgsConstructor
public class NoteService {
    private final MergeRequestCommentRepository mergeRequestCommentRepository;
    private final IssueCommentRepository issueCommentRepository;

    public List<NoteView> getMergeRequestNotes(Long projectId, OffsetDateTime startDateTime, OffsetDateTime endDateTime) {
        return mergeRequestCommentRepository
                .findAllByProjectIdAndDateRange(projectId, startDateTime, endDateTime)
                .stream()
                .map(NoteView::fromNote)
                .collect(Collectors.toList());
    }

    public List<NoteView> getIssueNotes(Long projectId, OffsetDateTime startDateTime, OffsetDateTime endDateTime) {
        return issueCommentRepository
                .findAllByProjectIdAndDateRange(projectId, startDateTime, endDateTime)
                .stream()
                .map(NoteView::fromNote)
                .collect(Collectors.toList());
    }
}
