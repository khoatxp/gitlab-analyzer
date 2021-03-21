import React, {useState} from 'react';
import {MergeRequest} from "../../interfaces/GitLabMergeRequest";
import DiffItemList, {DiffItem} from "./DiffItemList";

type MergeRequestListProps = {
    mergeRequests: MergeRequest[]
    handleSelectMergeRequest: (mergeRequest: MergeRequest) => void;
}

const MergeRequestList = ({mergeRequests, handleSelectMergeRequest}: MergeRequestListProps) => {
    const [selectedIndex, setSelectedIndex] = useState<number>(-1); // Start with invalid index so nothing is selected

    const diffItems = mergeRequests.map((mergeRequest) => {
        let diffItem: DiffItem = {
            id: mergeRequest.id.toString(),
            createdAt: mergeRequest.created_at,
            authorName: mergeRequest.author.name,
            title: mergeRequest.title,
        }
        return diffItem;
    });

    const handleSelectDiffItem = (diffItem: DiffItem) => {
        const mergeRequest = mergeRequests.find((mergeRequest) => {
            return mergeRequest.id.toString() == diffItem.id;
        })
        if (!mergeRequest) {
            return;
        }
        handleSelectMergeRequest(mergeRequest);
    }

    return (
        <DiffItemList
            diffItems={diffItems}
            diffItemType="Merge Request"
            handleSelectDiffItem={handleSelectDiffItem}
            selectedIndex={selectedIndex}
            setSelectedIndex={setSelectedIndex}
        />
    );
}

export default MergeRequestList;