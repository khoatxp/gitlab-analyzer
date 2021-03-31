package com.eris.gitlabanalyzer.service;

import com.eris.gitlabanalyzer.repository.IssueCommentRepository;
import com.eris.gitlabanalyzer.repository.MergeRequestCommentRepository;
import com.eris.gitlabanalyzer.viewmodel.NoteView;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@lombok.RequiredArgsConstructor
public class NoteService {
    private final MergeRequestCommentRepository mergeRequestCommentRepository;
    private final IssueCommentRepository issueCommentRepository;

    public List<NoteView> getMergeRequestNotes(Long projectId) {
        return mergeRequestCommentRepository
                .findAllByProjectId(projectId)
                .stream()
                .map(NoteView::fromNote)
                .collect(Collectors.toList());
    }

    public List<NoteView> getIssueNotes(Long projectId) {
        return issueCommentRepository
                .findAllByProjectId(projectId)
                .stream()
                .map(NoteView::fromNote)
                .collect(Collectors.toList());
    }
}
