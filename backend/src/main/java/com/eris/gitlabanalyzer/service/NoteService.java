package com.eris.gitlabanalyzer.service;

import com.eris.gitlabanalyzer.model.Note;
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
        return mapNotesToNoteView(
                mergeRequestCommentRepository.findAllByProjectIdAndDateRange(projectId, startDateTime, endDateTime)
        );
    }

    public List<NoteView> getGitManagementUserMergeRequestNotes(Long projectId, Long gitManagementUserId, OffsetDateTime startDateTime, OffsetDateTime endDateTime) {
        return mapNotesToNoteView(
                mergeRequestCommentRepository.findAllByProjectIdAndGitManagementUserIdAndDateRange(projectId, gitManagementUserId, startDateTime, endDateTime)
        );
    }

    public List<NoteView> getIssueNotes(Long projectId, OffsetDateTime startDateTime, OffsetDateTime endDateTime) {
        return mapNotesToNoteView(
                issueCommentRepository.findAllByProjectIdAndDateRange(projectId, startDateTime, endDateTime)
        );
    }

    public List<NoteView> getGitManagementUserIssueNotes(
            Long projectId, Long gitManagementUserId,  OffsetDateTime startDateTime, OffsetDateTime endDateTime) {
        return mapNotesToNoteView(
                issueCommentRepository.findAllByProjectIdAndGitManagementUserIdAndDateRange(projectId, gitManagementUserId, startDateTime, endDateTime)
        );
    }

    private List<NoteView> mapNotesToNoteView(List<Note> notes) {
        return notes
                .stream()
                .map(NoteView::fromNote)
                .collect(Collectors.toList());
    }
}
